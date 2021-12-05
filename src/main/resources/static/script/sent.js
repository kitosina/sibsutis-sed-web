var app = angular.module("SENT",[]);

app.controller("SENT_CONTROLLER",function($scope, $http) {
    $scope.sentAll = []

    $scope.init = function () {
        var url = '/document/sent/message';
        console.log("request")
        $http({
            url: url,
            method: 'GET',
        }).then(function (response) {
            $scope.sentAll = response.data;
        })
    }

    $scope.sentDocument = function (sent) {
        var url = '/document/sent';
        $http({
            url: url,
            method: 'GET',
            params:{
                document_name: sent.document_name
            },
            responseType:'arraybuffer'
        }).then(function (response) {
            var file = new Blob([response.data], { type: 'application/pdf' });
            var fileURL = URL.createObjectURL(file);

            window.open(fileURL);
        })
    }
});
