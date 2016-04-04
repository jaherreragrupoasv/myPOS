'use strict';

angular.module('myappApp').controller('DiscountDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Discount', 'Category', 'Article',
        function ($scope, $stateParams, $uibModalInstance, entity, Discount, Category, Article) {

            $scope.discount = entity;
            $scope.categorys = Category.query();
            $scope.articles = Article.query();

            $scope.load = function (id) {
                Discount.get({id: id}, function (result) {
                    $scope.discount = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.$emit('myappApp:discountUpdate', result);
                $uibModalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                if ($scope.discount.id != null) {
                    Discount.update($scope.discount, onSaveSuccess, onSaveError);
                } else {
                    Discount.save($scope.discount, onSaveSuccess, onSaveError);
                }
            };

            $scope.clear = function () {
                $uibModalInstance.dismiss('cancel');
            };
            $scope.datePickerForFromDate = {};

            $scope.datePickerForFromDate.status = {
                opened: false
            };

            $scope.datePickerForFromDateOpen = function ($event) {
                $scope.datePickerForFromDate.status.opened = true;
            };
            $scope.datePickerForToDate = {};

            $scope.datePickerForToDate.status = {
                opened: false
            };

            $scope.datePickerForToDateOpen = function ($event) {
                $scope.datePickerForToDate.status.opened = true;
            };
        }]);
