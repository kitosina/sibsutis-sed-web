var app = angular.module("SIGN",[]);

app.controller("SIGN_CONTROLLER",function($scope, $http) {
    $scope.signedAll = []

    $scope.init = function () {
        var url = '/sign/document';
        $http({
            url: url,
            method: 'GET',
            params:{
                sign_flag: true,
            },
        }).then(function (response) {
            console.log(response.data)
            $scope.signedAll = response.data;
        })
    }

    $scope.signedDocument = function (signed) {
        var url = '/document/sign';
        $http({
            url: url,
            method: 'GET',
            params:{
                document_name: signed.document_name,
                sign_flag: true
            },
            responseType:'arraybuffer'
        }).then(function (response) {
            var file = new Blob([response.data], { type: 'application/pdf' });
            var fileURL = URL.createObjectURL(file);

            window.open(fileURL);
        })
    }

});
