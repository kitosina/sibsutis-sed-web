var app = angular.module("SEND",[]);

app.controller("SEND_CONTROLLER",function($scope, $http) {

    $scope.receiverAll = []

    $scope.init = function () {
        var url = '/user/receivers';
        console.log("request")
        $http({
            url: url,
            method: 'GET',
        }).then(function (response) {
            console.dir(response.data)
            $scope.receiverAll = response.data;
        })
    }

    $scope.sendDocument = function (receiver) {
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
        }).then(function (response) {
            alert("Документ отправлен")
            window.location.reload()
        }).error(function (response) {
            alert("Ошибка отправки документа")
        });
    }

});
