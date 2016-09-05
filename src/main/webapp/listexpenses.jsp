<%--
Copyright 2015 Niklas Polke

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
--%>
<%@page language="Java" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="header.jsp"/>


<h3>List Expenses</h3>

<div class="w3-panel w3-padding-8">
<fmt:setLocale value="de_DE"/>
<table class="w3-table-all">
    <thead>
        <tr>
            <c:if test="${requestScope.pageMax gt 1}"><th class="w3-light-grey" colspan="5">
                <c:choose>
                <c:when test="${requestScope.page eq 1}">
                    <img src="img/sign-left_24_inactive.png" alt="inactive arrow left" title="no previous results" width="24" height="24"/>
                </c:when>
                <c:otherwise>
                    <a href="listexpenses?page=${requestScope.page - 1}"><img src="img/sign-left_24.png" alt="active arrow left" title="go to previous results" width="24" height="24"/></a>
                </c:otherwise>
                </c:choose>
                <c:choose>
                <c:when test="${requestScope.page >= requestScope.pageMax}">
                    <img src="img/sign-right_24_inactive.png" alt="inactive arrow right" title="no further results" width="24" height="24"/>
                </c:when>
                <c:otherwise>
                    <a href="listexpenses?page=${requestScope.page + 1}"><img src="img/sign-right_24.png" alt="active arrow right" title="go to further results" width="24" height="24"/></a>
                </c:otherwise>
                </c:choose>
            </th></c:if>
        </tr>
        <tr>
            <th>Date</th>
            <th>Category</th>
            <th>Amount</th>
            <th>Reason</th>
            <th>Modify</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="expense" items="${sessionScope.expenses}">
            <tr>
                <td><c:out value="${expense.getReadableDayAsString()}"/></td>
                <td><c:out value="${expense.categoryName}"/></td>
                <td class="number"><fmt:formatNumber value="${expense.amount}" type="currency"/></td>
                <td><c:out value="${expense.reason}"/></td>
                <td style="border:none">
                    <a href="editexpense?id=${expense.id}"><img src="img/pencil_24.png" alt="edit expense" title="edit expense" width="24" height="24"/></a>
                    <a href="addexpense?id=${expense.id}"><img src="img/sign-add_24.png" alt="copy expense" title="copy expense" width="24" height="24"/></a>
                    <a id="delete${expense.id}" href="deleteexpense?id=${expense.id}" onclick="return prompt('delete${expense.id}', '${fn:replace(expense, '\"', '&quot;')}')"><img src="img/sign-delete_24.png" alt="delete expense" title="delete expense" width="24" height="24"/></a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
</div>

<script type="text/javascript">
function prompt(id, expense) {
    var confirmed = confirm("Attention: Unrecoverable Delete Action\n\nDo you really want to delete:\n" + expense + " ?");
    if (confirmed) {
        var deleteLink = document.getElementById(id);
        deleteLink.setAttribute('href', deleteLink.getAttribute('href') + '&confirmed=yes');
        return true;
    } else {
        return false;
    }
}
</script>


<jsp:include page="footer.jsp"/>
