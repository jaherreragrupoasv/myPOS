'use strict';

angular.module('myappApp').controller('SalePaymentDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'SalePayment',
        function($scope, $stateParams, $uibModalInstance, entity, SalePayment) {

        $scope.salePayment = entity;
        $scope.load = function(id) {
            SalePayment.get({id : id}, function(result) {
                $scope.salePayment = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('myappApp:salePaymentUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.salePayment.id != null) {
                SalePayment.update($scope.salePayment, onSaveSuccess, onSaveError);
            } else {
                SalePayment.save($scope.salePayment, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
