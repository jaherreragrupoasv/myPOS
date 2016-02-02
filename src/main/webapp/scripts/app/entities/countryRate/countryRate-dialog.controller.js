'use strict';

angular.module('myappApp').controller('CountryRateDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'CountryRate',
        function($scope, $stateParams, $uibModalInstance, entity, CountryRate) {

        $scope.countryRate = entity;
        $scope.load = function(id) {
            CountryRate.get({id : id}, function(result) {
                $scope.countryRate = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('myappApp:countryRateUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.countryRate.id != null) {
                CountryRate.update($scope.countryRate, onSaveSuccess, onSaveError);
            } else {
                CountryRate.save($scope.countryRate, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
