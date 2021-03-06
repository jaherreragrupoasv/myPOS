'use strict';

angular.module('myappApp').controller('ArticleDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Article', 'Category',
        function($scope, $stateParams, $uibModalInstance, entity, Article, Category) {

        $scope.article = entity;
        $scope.categorys = Category.query();
        $scope.load = function(id) {
            Article.get({id : id}, function(result) {
                $scope.article = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('myappApp:articleUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.article.id != null) {
                Article.update($scope.article, onSaveSuccess, onSaveError);
            } else {
                Article.save($scope.article, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
