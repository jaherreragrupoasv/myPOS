'use strict';

angular.module('myappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('discountSale', {
                parent: 'entity',
                url: '/discountSales',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'myappApp.discountSale.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/discountSale/discountSales.html',
                        controller: 'DiscountSaleController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('discountSale');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('discountSale.detail', {
                parent: 'entity',
                url: '/discountSale/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'myappApp.discountSale.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/discountSale/discountSale-detail.html',
                        controller: 'DiscountSaleDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('discountSale');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'DiscountSale', function($stateParams, DiscountSale) {
                        return DiscountSale.get({id : $stateParams.id});
                    }]
                }
            })
            .state('discountSale.new', {
                parent: 'discountSale',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/discountSale/discountSale-dialog.html',
                        controller: 'DiscountSaleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    amount: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('discountSale', null, { reload: true });
                    }, function() {
                        $state.go('discountSale');
                    })
                }]
            })
            .state('discountSale.edit', {
                parent: 'discountSale',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/discountSale/discountSale-dialog.html',
                        controller: 'DiscountSaleDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['DiscountSale', function(DiscountSale) {
                                return DiscountSale.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('discountSale', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('discountSale.delete', {
                parent: 'discountSale',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/discountSale/discountSale-delete-dialog.html',
                        controller: 'DiscountSaleDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['DiscountSale', function(DiscountSale) {
                                return DiscountSale.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('discountSale', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
