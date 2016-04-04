'use strict';

angular.module('myappApp').controller('SaleLineDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'SaleLine', 'Sale', 'Article',
        function ($scope, $stateParams, $uibModalInstance, entity, SaleLine, Sale, Article) {

            $scope.saleLine = entity;
            $scope.articles = Article.query();
            $scope.sales = Sale.query();
            $scope.load = function (id) {
                SaleLine.get({id: id}, function (result) {
                    $scope.saleLine = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('myappApp:saleLineUpdate', result);
                $uibModalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                if ($scope.saleLine.id != null) {
                    SaleLine.update($scope.saleLine, onSaveSuccess, onSaveError);
                } else {
                    SaleLine.save($scope.saleLine, onSaveSuccess, onSaveError);
                }
            };

            $scope.clear = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }]);
