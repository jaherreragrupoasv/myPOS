'use strict';

angular.module('myappApp')
	.controller('DiscountSaleDeleteController', function($scope, $uibModalInstance, entity, DiscountSale) {

        $scope.discountSale = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            DiscountSale.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
