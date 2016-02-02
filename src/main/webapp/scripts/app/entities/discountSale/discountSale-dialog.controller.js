'use strict';

angular.module('myappApp').controller('DiscountSaleDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'DiscountSale',
        function($scope, $stateParams, $uibModalInstance, entity, DiscountSale) {

        $scope.discountSale = entity;
        $scope.load = function(id) {
            DiscountSale.get({id : id}, function(result) {
                $scope.discountSale = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('myappApp:discountSaleUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.discountSale.id != null) {
                DiscountSale.update($scope.discountSale, onSaveSuccess, onSaveError);
            } else {
                DiscountSale.save($scope.discountSale, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
