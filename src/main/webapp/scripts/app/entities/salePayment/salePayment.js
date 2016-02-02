'use strict';

angular.module('myappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('salePayment', {
                parent: 'entity',
                url: '/salePayments',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'myappApp.salePayment.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/salePayment/salePayments.html',
                        controller: 'SalePaymentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('salePayment');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('salePayment.detail', {
                parent: 'entity',
                url: '/salePayment/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'myappApp.salePayment.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/salePayment/salePayment-detail.html',
                        controller: 'SalePaymentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('salePayment');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SalePayment', function($stateParams, SalePayment) {
                        return SalePayment.get({id : $stateParams.id});
                    }]
                }
            })
            .state('salePayment.new', {
                parent: 'salePayment',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/salePayment/salePayment-dialog.html',
                        controller: 'SalePaymentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    creditCard: null,
                                    date: null,
                                    amount: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('salePayment', null, { reload: true });
                    }, function() {
                        $state.go('salePayment');
                    })
                }]
            })
            .state('salePayment.edit', {
                parent: 'salePayment',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/salePayment/salePayment-dialog.html',
                        controller: 'SalePaymentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SalePayment', function(SalePayment) {
                                return SalePayment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('salePayment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('salePayment.delete', {
                parent: 'salePayment',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/salePayment/salePayment-delete-dialog.html',
                        controller: 'SalePaymentDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['SalePayment', function(SalePayment) {
                                return SalePayment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('salePayment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
