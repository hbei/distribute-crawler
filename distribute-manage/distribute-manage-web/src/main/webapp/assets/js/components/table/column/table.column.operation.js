//------------------------------------------------------
//
//
//
//
//
//------------------------------------------------------
angular.module('admin.component')
    .directive('uiTableOperationColumn', function (UITableColumnControl) {
        class UITableOperationColumnControl extends UITableColumnControl {
            constructor(s, e, a, t) {
                this.className = 'OperationColumn';
                super(s, e, a, t);
            }

            init() {
                super.init();
            }
        }
        return {
            restrict: 'E',
            replace: true,
            transclude: true,
            scope: {
                head: '@'
            },
            controller: function ($scope, $element, $attrs, $transclude) {
                return new UITableOperationColumnControl($scope, $element, $attrs, $transclude);
            },
            template: `
                <th>
                    {{head}}
                </th>'
            `
        };
    });