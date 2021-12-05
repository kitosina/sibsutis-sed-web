var app = angular.module("SEND",[]);

app.controller("SEND_CONTROLLER",function($scope, $http) {
    $scope.sendDocument = function () {
        var data = new FormData();
        var url = '/document/send';

        var documentFile = document.getElementById('pdfFile').files[0]
        var emailReceiver = document.getElementById('emailReceiver').value

        data.append('document', documentFile)
        data.append('email_receiver', emailReceiver)

        $http({
            url: url,
            method: 'POST',
            data: data,
            transformRequest: angular.identity,
            transformResponse: angular.identity,
            headers : {
                'Content-Type': undefined
            }
        });
    }

});
