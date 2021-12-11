var app = angular.module("CONTRAGENT",[]);

app.controller("CONTRAGENT_CONTROLLER",function($scope, $http) {
    $scope.contragentAll = []

    $scope.init = function () {
        var url = '/user/contragent';
        $http({
            url: url,
            method: 'GET',
        }).then(function (response) {
            console.log(response.data)
            $scope.contragentAll = response.data;
        })
    }

});
