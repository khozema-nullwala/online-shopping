$(function() {
	// solving the active menu problem
	switch(menu) {
	
		case 'About Us':
			$('#about').addClass('active');
			break;
		case 'Contact Us':
			$('#contact').addClass('active');
			break;
		case 'All Products':
			$('#listProducts').addClass('active');
			break;
		default:	
			if(menu == "Home") break;
			$('#listProducts').addClass('active');
			$('#a_'+menu).addClass('active');
			break;		
	}
	
	
	// code for jquery dataTable

	
	var $table = $('#productListTable');
	
	// execute the below code only where we have this table
	if($table.length) {
		//console.log('Inside the table!');
		
		var jsonUrl = '';
		if(window.categoryId == '') {
			jsonUrl = window.contextRoot + '/json/data/all/products';
		}
		else {
			jsonUrl = window.contextRoot + '/json/data/category/'+ window.categoryId +'/products';
		}
			
		
		
		$table.DataTable( {
			
			lengthMenu: [[3,5,10,-1], ['3 Records', '5 Records', '10 Records', 'ALL']],
			pageLength: 5,
			ajax: {
				url: jsonUrl,
				dataSrc: ''
			},
			columns: [
			          {
			        	  data: 'code',
			        	  bSortable: false,
			        	  mRender: function(data, type, row) {
			        		  
			        		  return '<img src="'+window.contextRoot+'/resources/images/'+data+'.jpg" class="dataTableImg"/>';
			        		  
			        	  }
			          },
			          {
			        	  data: 'name'			        	  
			          },
			          {
			        	  data: 'brand'			        	  
			          },
			          {
			        	  data: 'unitPrice',
			        	  mRender: function(data, type, row) {
			        		  return '&#8377; ' + data
			        	  }
			          },
			          {
			        	  data: 'quantity',
			        	  mRender: function(data, type, row) {
			        		
			        		  if(data < 1) {
			        			  return '<span style="color:red">Out of Stock!</span>';
			        		  }
			        		  
			        		  return data;
			        		  
			        	  }
			          },
			          {
			        	  data: 'id',
			        	  bSortable: false,
			        	  mRender: function(data, type, row) {
			        		  
			        		  var str = '';
			        		  str += '<a href="'+window.contextRoot+ '/show/'+data+'/product" class="btn btn-primary"><span class="glyphicon glyphicon-eye-open"></span></a> &#160;';
			        		  
			        		  if(row.quantity < 1) {
			        			  str += '<a href="javascript:void(0)" class="btn btn-success disabled"><span class="glyphicon glyphicon-shopping-cart"></span></a>';
			        		  }
			        		  else {
			        			  
			        			  str += '<a href="'+window.contextRoot+ '/cart/add/'+data+'/product" class="btn btn-success"><span class="glyphicon glyphicon-shopping-cart"></span></a>';  
			        		  }
			        		  
			        		  			        		  
			        		  
			        		  return str;
			        		  
			        	  }
			        	  
			          }
			          ]
		});
	}
});