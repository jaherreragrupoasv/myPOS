'use strict';

angular.module('myappApp').controller('SaleDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sale', 'SaleLine',
        function($scope, $stateParams, $uibModalInstance, entity, Sale, SaleLine) {

        $scope.sale = entity;
        $scope.salelines = SaleLine.query();
        $scope.load = function(id) {
            Sale.get({id : id}, function(result) {
                $scope.sale = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('myappApp:saleUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.sale.id != null) {
                Sale.update($scope.sale, onSaveSuccess, onSaveError);
            } else {
                Sale.save($scope.sale, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
