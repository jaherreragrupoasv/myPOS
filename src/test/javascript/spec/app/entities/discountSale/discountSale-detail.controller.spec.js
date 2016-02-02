'use strict';

describe('DiscountSale Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockDiscountSale;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockDiscountSale = jasmine.createSpy('MockDiscountSale');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'DiscountSale': MockDiscountSale
        };
        createController = function() {
            $injector.get('$controller')("DiscountSaleDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'myappApp:discountSaleUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
