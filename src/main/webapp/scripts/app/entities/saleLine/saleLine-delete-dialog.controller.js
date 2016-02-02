'use strict';

angular.module('myappApp')
	.controller('SaleLineDeleteController', function($scope, $uibModalInstance, entity, SaleLine) {

        $scope.saleLine = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            SaleLine.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
