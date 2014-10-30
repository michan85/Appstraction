 (function() {
 'use strict';
 
var app = angular.module('app', ['ui.tree', 'ui.ace']);

app.controller('EditorController', [ '$scope', function($scope) {

// The modes
    $scope.modes = ['Html', 'XML', 'Javascript'];
    $scope.mode = $scope.modes[0];


  $scope.aceLoaded = function(_editor) {
    console.log( "aceLoaded", arguments, this);
  };

  $scope.aceChanged = function(e) {
    	console.log( "aceChanged", arguments, this);
  };

}]);

function LiveDoc( dom ){
	var me = this;
	this.dom = dom;
	this.nodex = {};
	this.id = 0;
	this.newId = function(){
		return ++me.id;
	}
	indexNode(dom,this);
	
	this.appendChild = function(parent, child){
		parent.childNodes = parent.childNodes || [];
		indexNode(child,me,parent.id);
		parent.childNodes.push(child);
	}
	
	this.remove = function(node){
		if(node.parentId){
			var list = me.nodex[node.parentId].childNodes || [];
			for(var i = list.length - 1; i >= 0; i--){
			    if(list[i].id == node.id){
			    	list.splice(i,1);
			    	break;
			    }
			}
		}
	}
}

function indexNode( node, doc, parentId ){
	node.id = node.id || doc.newId(node) ;
	doc.nodex[node.id] = node;
	if(parentId)
		node.parentId = parentId;
	if(node.childNodes){
		for(var i=0,child,l=node.childNodes.length; (i < l  && (child=node.childNodes[i])); i++){
			indexNode( child,doc,node.id );
		}
	}
}




window.LiveDoc = LiveDoc;
window.DomDoc = new LiveDoc({
	type: "document",
	childNodes: [
		{ type: "relative-layout", childNodes:[
			{ type: "text"},
			{ type: "button"}
		] }
	]
});

window.app = app;

app.controller('DomInspector', function($scope) {
   
	window.domInspector = $scope;
	var doc = DomDoc;
	
	$scope.dom = [DomDoc.dom];
	
	

    $scope.selectedItem = {};

    $scope.options = {
    	dropped: function(){
    		console.log("dropped",this,arguments);
    	}
    };

    $scope.remove = function(scope) {
      var node = scope.$modelValue;
      doc.remove(node);
      //scope.remove();
    };

    $scope.toggle = function(scope) {
      scope.toggle();
    };

    $scope.appendChild = function(scope) {
      var node = scope.$modelValue;
      doc.appendChild(node, {
    	  type: "new"
      } );
//      DomDoc.appendChild(  );
//      nodeData.items.push({
//        id: nodeData.id * 10 + nodeData.items.length,
//        title: nodeData.title + '.' + (nodeData.items.length + 1),
//        items: []
//      });
    };
  });
  
  })();