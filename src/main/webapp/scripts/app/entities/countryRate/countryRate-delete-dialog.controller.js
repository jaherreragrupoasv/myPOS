'use strict';

angular.module('myappApp')
	.controller('CountryRateDeleteController', function($scope, $uibModalInstance, entity, CountryRate) {

        $scope.countryRate = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            CountryRate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
