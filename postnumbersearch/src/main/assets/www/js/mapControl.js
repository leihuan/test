//waiting();

var map;
var myGeo = new BMap.Geocoder();
initMap();
function initMap(){
// 百度地图API功能

    map = new BMap.Map("allmap");    // 创建Map实例
    map.centerAndZoom(new BMap.Point(116.404, 39.915), 9);  // 初始化地图,设置中心点坐标和地图级别
    map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
    map.setCurrentCity("北京");          // 设置地图显示的城市 此项是必须设置的
    map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放

}

//java端调用
function showResultOnMap(addr){
	alert(addr)
	map.setCenter(addr);
}

function myFun(result){
	//waited();
	var cityname = result.name;
	map.setCenter(cityname);
	ps.setLocations(cityname);
	ps.dissmissProgressBar();
	}
	var myCity = new BMap.LocalCity();
	myCity.get(myFun);

function setAddress(result){
   map.centerAndZoom(result,9);
}
// 百度地图API功能
function G(id) {
	return document.getElementById(id);
}
function waiting(){
	G("waiting").style.display = "block";
};

function waited(){
	G("waiting").style.display = "none";
};

