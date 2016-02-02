'use strict';

angular.module('myappApp')
	.controller('DiscountSaleLineDeleteController', function($scope, $uibModalInstance, entity, DiscountSaleLine) {

        $scope.discountSaleLine = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            DiscountSaleLine.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
