function addToCart(bookid)
{
   
   if (confirm("장바구니에 도서를 추가합니까?")==true){
	
		document.addForm.action="/BookMarket/cart/book/"+bookid;
   		document.addForm.submit();
	
   
   }
   
    
}
function removeFromCart(bookid, cartId) {
	
	   document.removeForm.action = "/BookMarket/cart/book/"+bookid;		
	   document.removeForm.submit();	  
	   setTimeout('location.reload()',10); 
	}
function clearCart(cartId) {
	if (confirm("모든 도서를 장바구니에서 삭제합니까?")==true){
		
	   document.clearForm.submit();
	   setTimeout('location.reload()',10); 
	
	}
	
}

function deleteConfirm(bookId){
if(confirm("삭제합니다.")==true){
// ✨ 수정: Context Path 변수를 사용하여 경로를 구성합니다.
        // Context Path가 /BookMarket이라면, 경로는 /BookMarket/books/delete/ISBN1234가 됩니다.
        location.href = contextPath + "books/delete/" + bookId;
}
else{return;}
}
