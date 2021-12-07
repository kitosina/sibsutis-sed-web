var app = angular.module("REF",[]);

app.controller("REF_CONTROLLER",function($scope, $http) {
    $scope.refusalAll = []

    $scope.init = function () {
        var url = '/sign/document';
        $http({
            url: url,
            method: 'GET',
            params:{
                sign_flag: false,
            },
        }).then(function (response) {
            console.log(response.data)
            $scope.refusalAll = response.data;
        })
    }

    $scope.refusalDocument = function (refusal) {
        var url = '/document/sign';
        $http({
            url: url,
            method: 'GET',
            params:{
                document_name: refusal.document_name,
                sign_flag: false
            },
            responseType:'arraybuffer'
        }).then(function (response) {
            var file = new Blob([response.data], { type: 'application/pdf' });
            var fileURL = URL.createObjectURL(file);

            window.open(fileURL);
        })
    }

});
