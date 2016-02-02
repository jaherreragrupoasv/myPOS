'use strict';

angular.module('myappApp').controller('DiscountSaleLineDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'DiscountSaleLine',
        function($scope, $stateParams, $uibModalInstance, entity, DiscountSaleLine) {

        $scope.discountSaleLine = entity;
        $scope.load = function(id) {
            DiscountSaleLine.get({id : id}, function(result) {
                $scope.discountSaleLine = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('myappApp:discountSaleLineUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.discountSaleLine.id != null) {
                DiscountSaleLine.update($scope.discountSaleLine, onSaveSuccess, onSaveError);
            } else {
                DiscountSaleLine.save($scope.discountSaleLine, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
