//-----------------------------------------------------------------------------------------------
//
//
//
//
//
//-----------------------------------------------------------------------------------------------
angular.module('admin.component')
    .directive('uiTree', function (UITreeControl) {
        return {
            restrict: 'E',
            replace: true,
            transclude: true,
            scope: {
                onBeforeClick: '&',
                onClick: '&',
                onBeforeCheck: '&',
                onDataSuccess: '&',
                onDataFail: '&',
                onCheck: '&',

                onComplete: '&',

                onAdd: '&',
                onEdit: '&',
                onRemove: '&',

                model: '=',

                checked: '=',
                filter: '='
            },
            compile: function () {
                var uiTree = null;
                return {
                    pre: function (s, e, a, c, t) {
                        uiTree = new UITreeControl(s, e, a, t);
                    },
                    post: function () {
                        uiTree.build();
                    }
                };
            },
            template: `
                <div class="ui-tree">
                    <ul class="ztree"></ul>
                    <span style="display:none">
                        <span ng-if="component.attrs.onAdd" class="button add" ng-click="onAddHandler($event)"></span>
                        <span ng-if="component.attrs.onEdit" class="button edit" ng-click="onEditHandler($event)"></span>
                        <span ng-if="component.attrs.onRemove" class="button remove" ng-click="onRemoveHandler($event)"></span>
                    </span>
                </div>
            `
        };
    });