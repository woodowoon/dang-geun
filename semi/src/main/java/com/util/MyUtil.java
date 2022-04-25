package com.util;

public class MyUtil {
	public int pageCount(int rows, int dataCount) {
		/**
		 * 전체 페이지 수 구하기
		 * @param rows		한화면에 출력할 목록 수
		 * @param dataCount	총 데이터 개수
		 * @return			전체 페이지 수
		 */
		if(dataCount <= 0) {
			return 0;
		}
			
		return dataCount / rows + (dataCount % rows > 0 ? 1: 0);
	}
	
	public String paging(int current_page, int total_page, String list_url) {
		StringBuilder sb = new StringBuilder();
		
		int numPerBlock = 10;
		int currentPageSetup;
		int n, page;
		
		if(current_page < 1 || total_page < current_page) {
			return "";
		}
		
		if(list_url.indexOf("?") != -1) {
			list_url += "&";
		} else {
			list_url += "?";
		}
		
		currentPageSetup = (current_page / numPerBlock) * numPerBlock;
		if(current_page % numPerBlock == 0) {
			currentPageSetup = currentPageSetup - numPerBlock;
		}

		sb.append("<div class='paginate'>");
		n = current_page - numPerBlock;
		if(total_page > numPerBlock && currentPageSetup > 0) {
			sb.append("<a href='" + list_url + "page=1'>처음</a>");
			sb.append("<a href='" + list_url + "page=" + n + "'>이전</a>");
		}

		page = currentPageSetup + 1;
		while(page <= total_page && page <= (currentPageSetup + numPerBlock)) {
			if(page == current_page) {
				sb.append("<span>" + page + "</span>");
			} else {
				sb.append("<a href='" + list_url + "page=" + page + "'>" + page + "</a>");
			}
			page++;
		}
		
		n = current_page + numPerBlock;
		if(n > total_page) n = total_page;
		if(total_page - currentPageSetup > numPerBlock) {
			sb.append("<a href='" + list_url + "page=" + n + "'>다음</a>");
			sb.append("<a href='" + list_url + "page=" + total_page + "'>끝</a>");
		}
		sb.append("</div>");
	
		return sb.toString();
	}

	/**
	 * javascript로 페이징(paging) 처리 : javascript 지정 함수 호출
	 * @param current_page	현재 표시되는 페이지 번호	
	 * @param total_page	전체 페이지 수
	 * @param methodName	호출할 자바 스크립트 함수명
	 * @return				페이징 처리 결과
	 */
    public String pagingMethod(int current_page, int total_page, String methodName) {
    	StringBuilder sb = new StringBuilder();

        int numPerBlock = 10;
        int currentPageSetUp;
        int n, page;
        
        if(current_page < 1 || total_page < current_page) {
        	return "";
        }
        
        currentPageSetUp = (current_page / numPerBlock) * numPerBlock;
        if (current_page % numPerBlock == 0) {
            currentPageSetUp = currentPageSetUp - numPerBlock;
        }

		sb.append("<div class='paginate'>");
        
        n = current_page - numPerBlock;
        if ((total_page > numPerBlock) && (currentPageSetUp > 0)) {
			sb.append("<a onclick='" + methodName + "(1);'>처음</a>");
			sb.append("<a onclick='" + methodName + "(" + n + ");'>이전</a>");
        }

        page = currentPageSetUp + 1;
        while((page <= total_page) && (page <= currentPageSetUp + numPerBlock)) {
           if(page == current_page) {
        	   sb.append("<span>" + page + "</span>");
           } else {
			   sb.append("<a onclick='" + methodName + "(" + page + ");'>" + page + "</a>");
           }
           page++;
        }

        n = current_page + numPerBlock;
		if(n > total_page) n = total_page;
        if (total_page - currentPageSetUp > numPerBlock) {
			sb.append("<a onclick='" + methodName + "(" + n + ");'>다음</a>");
			sb.append("<a onclick='" + methodName + "(" + total_page + ");'>끝</a>");
        }
		sb.append("</div>");

        return sb.toString();
    }

	public String pagingUrl(int current_page, int total_page, String list_url) {
		StringBuilder sb = new StringBuilder();
		
		int numPerBlock = 10;
		int n, page;
		
		if(current_page < 1 || total_page < current_page) {
			return "";
		}
		
		if(list_url.indexOf("?") != -1) {
			list_url += "&";
		} else {
			list_url += "?";
		}
		
		page = 1; 
		if(current_page > (numPerBlock / 2) + 1) {
			page = current_page - (numPerBlock / 2) ;
			
			n = total_page - page;
			if( n <= numPerBlock ) {
				page = total_page - numPerBlock + 1;
			}
			
			if(page < 1) page = 1;
		}
		
		sb.append("<div class='paginate'>");
		
		if(page > 1) {
			sb.append("<a href='"+list_url+"page=1' title='처음'>&#x226A</a>");
		}

		n = current_page - 1;
		if(current_page > 1) {
			sb.append("<a href='"+list_url+"page="+n+"' title='이전'>&#x003C</a>");
		}

		n = page;
		while(page <= total_page && page < n + numPerBlock) {
			if(page == current_page) {
				sb.append("<span>"+page+"</span>");
			} else {
				sb.append("<a href='"+list_url+"page="+page+"'>"+page+"</a>");
			}
			page++;
		}

		n = current_page + 1;
		if(current_page < total_page) {
			sb.append("<a href='"+list_url+"page="+n+"' title='다음'>&#x003E</a>");
		}

		if(page <= total_page) {
			sb.append("<a href='"+list_url+"page="+total_page+"' title='마지막'>&#x226B</a>");
		}
		
		sb.append("</div>");
		
		return sb.toString();
	}    
	
	public String pagingFunc(int current_page, int total_page, String methodName) {
		StringBuilder sb = new StringBuilder();
		
		int numPerBlock = 10;
		int n, page;
		
		if(current_page < 1 || total_page < current_page) {
			return "";
		}
		
		page = 1; 
		if(current_page > (numPerBlock / 2) + 1) {
			page = current_page - (numPerBlock / 2) ;
			
			n = total_page - page;
			if( n <= numPerBlock ) {
				page = total_page - numPerBlock + 1;
			}
			
			if(page < 1) page = 1;
		}
		
		sb.append("<div class='paginate'>");
		
		if(page > 1) {
			sb.append("<a onclick='" + methodName + "(1);' title='처음'>&#x226A</a>");
		}

		n = current_page - 1;
		if(current_page > 1) {
			sb.append("<a onclick='" + methodName + "(" + n + ");' title='이전'>&#x003C</a>");
		}

		n = page;
		while(page <= total_page && page < n + numPerBlock) {
			if(page == current_page) {
				sb.append("<span>"+page+"</span>");
			} else {
				sb.append("<a onclick='" + methodName + "(" + page + ");' >"+page+"</a>");
			}
			page++;
		}
		
		n = current_page + 1;
		if(current_page < total_page) {
			sb.append("<a onclick='" + methodName + "(" + n + ");' title='다음'>&#x003E</a>");
		}

		if(page <= total_page) {
			sb.append("<a onclick='" + methodName + "(" + total_page + ");' title='마지막'>&#x226B</a>");
		}
		
		sb.append("</div>");
		
		return sb.toString();
	}    
	
	
    /**
     * 특수문자를 HTML 문자로 변경 및 엔터를 <br>로 변경
     * @param str	변경할 문자열
     * @return		HTML 문자로 변경된 문자열
     */
     public String htmlSymbols(String str) {
		if(str == null || str.length() == 0)
			return "";

    	 str=str.replaceAll("&", "&amp;");
    	 str=str.replaceAll("\"", "&quot;");
    	 str=str.replaceAll(">", "&gt;");
    	 str=str.replaceAll("<", "&lt;");
    	 
    	 str=str.replaceAll("\n", "<br>");
    	 str=str.replaceAll("\\s", "&nbsp;");  
    	 
    	 return str;
     }

     /**
      * NULL을 ""로 변경하기
      * @param str	변경할 문자열
      * @return		NULL을 ""로 변경된 문자열
      */
     public String checkNull(String str) {
         return str == null ? "" : str;
     }
}
