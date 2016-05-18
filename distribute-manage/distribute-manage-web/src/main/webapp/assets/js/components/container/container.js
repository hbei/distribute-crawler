//-----------------------------------------------------------------------------------------------
//
//
//
//
//-----------------------------------------------------------------------------------------------
(function () {

    angular.module('admin.component')
        .directive('uiContainer', function ($timeout, $controller, $injector, Logger) {

            class UIContainer extends Event {

                constructor(scope, element, attrs, $transclude) {
                    this.scope = scope;
                    this.element = element;
                    this.attrs = attrs;
                    this.transclude = $transclude;
                    this.logger = new Logger('UIContainer');
                }

                init() {
                    this.scope.$parent.$on('componentComplete', this.initHandler.bind(this));
                    this.content = this.transclude(this.scope.$parent);
                    this.element
                        .show()
                        .append(this.content);
                }

                initController() {
                    var ctrl = this.scope.controller;
                    $timeout(() => {
                        // 全局定义
                        if (ctrl && window[ctrl]) {
                            var ctrlArgs = /\(([^\)]+)\)/.exec(window[ctrl].toString())[1],
                                args = {$scope: this.scope.$parent};
                            ctrlArgs = ctrlArgs.split(',');
                            for (var i = 1, arg; i < ctrlArgs.length; i++) {
                                arg = $.trim(ctrlArgs[i]);
                                args[arg] = $injector.get(arg);
                            }
                            $controller(window[ctrl], args);
                        }
                        //
                        else if (ctrl) {
                            $controller(ctrl, {$scope: this.scope.$parent});
                        }

                        //
                        this.scope.onComplete({});
                    });
                }

                initHandler(evt, args) {
                    if (args) {
                        let $parent = this.scope.$parent;
                        if ($parent[args.ref]) {
                            $parent[args.ref] = [].concat($parent[args.ref]);
                            $parent[args.ref].push(args.component);
                        }
                        else {
                            $parent[args.ref] = args.component;
                        }
                        this.logger.debug(`${args.ref} => ${$parent[args.ref]}`);
                    }
                }
            }

            return {
                restrict: 'E',
                replace: true,
                transclude: true,
                scope: {
                    controller: '@',
                    onComplete: '&'
                },
                compile: function () {
                    var uiContainer = null;
                    return {
                        pre: function (scope, element, attrs, controller, transclude) {
                            uiContainer = new UIContainer(scope, element, attrs, transclude);
                            uiContainer.init();
                        },
                        post: function () {
                            uiContainer.initController();
                        }
                    };
                },
                template: `
                    <div></div>
                `
            };
        });
})();