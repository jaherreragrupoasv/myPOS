'use strict';

angular.module('myappApp')
	.controller('SalePaymentDeleteController', function($scope, $uibModalInstance, entity, SalePayment) {

        $scope.salePayment = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            SalePayment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
