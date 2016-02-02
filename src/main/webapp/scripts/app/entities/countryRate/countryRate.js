'use strict';

angular.module('myappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('countryRate', {
                parent: 'entity',
                url: '/countryRates',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'myappApp.countryRate.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/countryRate/countryRates.html',
                        controller: 'CountryRateController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('countryRate');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('countryRate.detail', {
                parent: 'entity',
                url: '/countryRate/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'myappApp.countryRate.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/countryRate/countryRate-detail.html',
                        controller: 'CountryRateDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('countryRate');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'CountryRate', function($stateParams, CountryRate) {
                        return CountryRate.get({id : $stateParams.id});
                    }]
                }
            })
            .state('countryRate.new', {
                parent: 'countryRate',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/countryRate/countryRate-dialog.html',
                        controller: 'CountryRateDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    country: null,
                                    rate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('countryRate', null, { reload: true });
                    }, function() {
                        $state.go('countryRate');
                    })
                }]
            })
            .state('countryRate.edit', {
                parent: 'countryRate',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/countryRate/countryRate-dialog.html',
                        controller: 'CountryRateDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CountryRate', function(CountryRate) {
                                return CountryRate.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('countryRate', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('countryRate.delete', {
                parent: 'countryRate',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/countryRate/countryRate-delete-dialog.html',
                        controller: 'CountryRateDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['CountryRate', function(CountryRate) {
                                return CountryRate.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('countryRate', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
