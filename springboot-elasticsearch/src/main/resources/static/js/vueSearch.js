
var suList = []
var dataList = []

var app = new Vue({
    el: '#wrapper',
    data: {
        message: 'Hello keyvin!',
        suList: suList,
        dataList: dataList,
        keyword: ''
    },
    methods: {
        selectLi: function(name) {
            this.keyword=name
        },
        showSuggetUl: function(flag) {
            if(flag){
                $("#suggetUl").show();
            }else{
                $("#suggetUl").hide();
            }
        },
        findSuggest:function(key){
            var _this = this
            if(_this.keyword!=0){
                $.ajax({
                    type: "GET",
                    url: "./search/suggester",
                    data: {
                        key: _this.keyword
                    },
                    dataType: "json",
                    success:function(data, status){
                        console.log("success:", data);
                        if(data.code==200){
                            _this.suList.splice(0)
                            if(data.body.length>0){
                                data.body.forEach(function (obj) {
                                    _this.suList.push(obj)
                                })
                                _this.showSuggetUl(true)
                            }else{
                                _this.showSuggetUl(false)
                            }
                        }
                    }
                })
            }else{
                _this.showSuggetUl(false)
            }
        },
        findData: function (keyword) {
            var _this = this
            $.ajax({
                type: "GET",
                url: "./search/list",
                data: {
                    keyword: _this.keyword
                },
                dataType: "json",
                success:function(data, status){
                    console.log("list success:", data);
                    if(data.code==200){
                        _this.dataList.splice(0)
                        data.body.list.forEach(function (obj) {
                            _this.dataList.push(obj)
                        })
                    }
                    console.log("dataList:", _this.dataList);
                }
            });
        }
    }
})
