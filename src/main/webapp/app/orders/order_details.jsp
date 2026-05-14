<jsp:useBean id="orderDetailsBean" class="com.genesys.webmenus.orders.OrderDetailsBean" scope="page"/>
<jsp:setProperty name="orderDetailsBean" property="*"/> 
<%
orderDetailsBean.setRequest(request); // pass request object to bean
orderDetailsBean.setOrderId(request.getParameter("id"));
orderDetailsBean.loadOrderDetails();
%>

<%@ taglib uri="/WEB-INF/tlds/webmenus.tld" prefix="webmenusCfg" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-1.3.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/js/jquery-ui-1.7.2.custom.min.js"></script>
        <link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath()%>/xlibs/jquery/css/redmond/jquery-ui-1.7.2.custom.css" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/includes/msgbox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/app/scripts/order_dashboard.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jquery-ui-timepicker-addon.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/xlibs/jquery/jquery-ui-sliderAccess.js"></script>
        <title>Order Details</title>
        <style>
            #main-console {
                width: 100%;
            }
            #main-console td {
                width: 50%;
            }
            .order-info tr td:first-child {
                text-align: right;
            }
            .button-cell {
                text-align: center;
                padding-top: 25px;
            }
            /* .save-btn {
                float: right;
                margin-right: 100px;
            } */
        </style>
        <script type="text/javascript">
            window.addEventListener("load", () => {
                $("#orderviewpanel").load("/webmenus/app/OrderView?oid=<%=request.getParameter("id")%>", function(){});
                $("#status").val("<%=orderDetailsBean.getStatus()%>")
                $("input[datatype='datetime'], .datatype_datetime").datetimepicker({
		            ampm: true
	            });
            });
            function onSave() {
                startAnimation();
                var status = $("#status").val();
                var invoice = $("#invoice").val();
                var estimated_time = $("#estimated_time").val();
                updateOrder("<%=request.getParameter("id")%>", status, invoice, estimated_time, () => {
                    //loadOrders("<%=request.getParameter("id")%>");
                    const myEvent = new CustomEvent('reLoadOrder', {});
                    window.parent.document.dispatchEvent(myEvent);

                    // Close modal
                    parent.$.fancybox.close();
                });
            }
        </script>
    </head>
    <body>
        <table id="main-console">
            <tr>
                <td>
                    <div id="client_area" class="ui-layout-center">
	                    <div id="orderviewpanel"></div>
                    </div>
                </td>
                <td>
                    <table class="order-info">
                        <tr>
                            <td>Status:</td>
                            <td><select id="status">
                                <option value="open">Open</option>
                                <option value="paymentpending">Payment Pending</option>
                                <option value="payment_failed">Payment Failed</option>
                                <option value="inprogress">Inprogress</option>
                                <option value="readyforpickup">Ready for Pickup</option>
                                <option value="outfordelivery">Out for Delivery</option>
                                <option value="complete">Complete</option>
                            </select></td>
                        </tr>
                        <tr>
                            <td>Invoice:</td>
                            <td><input id="invoice" type="text" value="<%=orderDetailsBean.getInvoice()%>"></input></td>
                        </tr>
                        <tr>
                            <td>Estimated Time:</td>
                            <td><input id="estimated_time" type="text" datatype="datetime" value="<%=orderDetailsBean.getEstimatedTime()%>"></input></td>
                        </tr>
                        <tr>
                            <td colspan="2" class="button-cell">
                                <button class="save-btn" onclick="parent.$.fancybox.close()">Close</button>
                                <button class="save-btn" onclick="onSave()">Save</button>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </body>
</html>
