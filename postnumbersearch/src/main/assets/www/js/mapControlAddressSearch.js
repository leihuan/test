//waiting();
var map;
initMap();
//当前地图标注点，marker
var centerMarker;
//当前地图标注点对应行政地址
var centerAddresse=new Array(3);
var myGeo = new BMap.Geocoder();
function initMap(){
// 百度地图API功能
	map = new BMap.Map("allmap");    // 创建Map实例
	map.centerAndZoom(new BMap.Point(116.404, 39.915), 9);  // 初始化地图,设置中心点坐标和地图级别
	map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
	map.setCurrentCity("北京");          // 设置地图显示的城市 此项是必须设置的
	map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放

}

// 添加带有定位的导航控件
var navigationControl = new BMap.NavigationControl({
	// 靠左上角位置
	anchor: BMAP_ANCHOR_TOP_LEFT,
	// LARGE类型
	type: BMAP_NAVIGATION_CONTROL_LARGE,
	// 启用显示定位
	enableGeolocation: true
});
map.addControl(navigationControl);

function myFun(result){
	var cityname = result.name;
	//alert(centerAddresse);
		map.setCenter(cityname);
	// 将地址解析结果显示在地图上,并调整地图视野
	myGeo.getPoint(cityname, function(point){
		if (point) {
			createMarker(point);
		}else{
			alert("您选择地址没有解析到结果!");
		}
	}, cityname);
//	ps.setLocation(cityname);
	ps.dissmissProgressBar();
	}
	var myCity = new BMap.LocalCity();
	myCity.get(myFun);

function setAddress(result){
   map.centerAndZoom(result,9);
}

function onSearchInfo(){
   //if(!centerAddresse.some){
	   ps.setLocation(centerAddresse);
	   //alert("centerAddresse"+centerAddresse);
   //}else{
   //    //alert("请输入查询地址");
	//   ps.setLocation(null);
   //}
}

var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
	{"input" : "suggestId"
		,"location" : map
	});

ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
	var str = "";
	var _value = e.fromitem.value;
	var value = "";
	if (e.fromitem.index > -1) {
		//value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
		value = _value.province +  _value.city +  _value.district;
	}
	str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

	value = "";
	if (e.toitem.index > -1) {
		_value = e.toitem.value;
		value = _value.province +  _value.city +  _value.district;
	}
	str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
	G("searchResultPanel").innerHTML = str;
});

var myValue;
ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
	waiting();
	var _value = e.item.value;
	myValue = _value.province +  _value.city +  _value.district;
	G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
	//ac.setInputValue(value);
	setPlace();
	waited();
});


function  createMarker(point){
	centerMarker = new BMap.Marker(point);// 创建标注
	map.addOverlay(centerMarker);             // 将标注添加到地图中
	centerMarker.enableDragging();           // 可拖拽
	centerMarker.addEventListener("dragend",getAttr);
	point2Address(point);
}


//获取marker点的位置信息
function getAttr(){
	waiting();
	var p = centerMarker.getPosition();       //获取marker的位置
	centerMarker.setAnimation(null);
	point2Address(p);
	waited();
}

function point2Address(point){
	myGeo.getLocation(point, function(rs){
		var addComp = rs.addressComponents;
		centerAddresse[0] = addComp.province;
		centerAddresse[1] = addComp.city;
		centerAddresse[2] = addComp.district;
		var value = addComp.province + ", " + addComp.city + ", " + addComp.district ;
		ac.setInputValue(value);
		G("clear").style.display="block";
	});
}

function setPlace(){
	map.clearOverlays();    //清除地图上所有覆盖物
	function myFun1(){
		var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
		map.centerAndZoom(pp, 9);
		createMarker(pp);    //添加标注
	}
	var local = new BMap.LocalSearch(map, { //智能搜索
		onSearchComplete: myFun1
	});
	local.search(myValue);
}

function inputOnChange(){
	//alert("inputOnChange");
	if(G("suggestId").value.length!= 0){
		G("clear").style.display="block";
	}else{
		G("clear").style.display="none";
	}
}

// 百度地图API功能
function G(id) {
	return document.getElementById(id);
}

function onClearClick(){
	ac.setInputValue("");
	centerAddresse[0] = "";
	centerAddresse[1] = "";
	centerAddresse[2] = "";
	G("clear").style.display="none";
}

function waiting(){
	G("waiting").style.display = "block";
};
function waited(){
	G("waiting").style.display = "none";
};