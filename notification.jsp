<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<title>EMPLOYEE MANAGEMENT SYSTEM</title>

<script type="text/javascript" src="resources/js/sorttable.js"></script>
<script src="resources/js/dataTables.pageResize.min.js"></script>
<!-- <script src="resources/js/dataTables.buttons.min.js"></script> -->
<script src="resources/js/buttons.flash.min.js"></script>
<script src="resources/js/jszip.min.js"></script>
<script src="resources/js/pdfmake.min.js"></script>
<script src="resources/js/vfs_fonts.js"></script>
<script src="resources/js/buttons.html5.min.js"></script>
<script src="resources/js/buttons.print.min.js"></script>
</head>
<body>
	<div class="container-fluid card" style="padding: 0">

		<div class="header mb-0">
			<h2>Notification Form</h2>
		</div>
		<div class="row">
			<form name="myNotificationForm" id="myNotificationForm"
				action="processForm" method="post" enctype="multipart/form-data"
				autocomplete="off" class="col-md-4"
				style="margin-top: 10px; padding: 10px 0 10px 10px">
				<div class="" style="padding: 10px; min-height: 400px; margin: 10px">
					<div class="border border-3 rounded-3">

						<div class="form-group mb-3">
							<label class="">Title </label> <input class="form-control"
								type="text" name="title" id="title">
						</div>

						<div class="form-group mb-3">

							<label class="">Upload Photo </label> <input class="form-control"
								id="file-input" name="image" type="file"
								accept="image/x-png,image/jpeg" multiple="multiple" />
						</div>

						<div class="form-group mb-3">
							<label class="">Message </label> <input class="form-control"
								type="text" name="message" id="message">
						</div>

						<!-- <button type="button"
        class="btn btn-primary"
        data-toggle="modal"
        data-target="#exampleModal">
        Show image
    </button>
   -->
						<!-- Modal -->
						<div class="modal fade" id="exampleModal" tabindex="-1"
							role="dialog" aria-labelledby="exampleModalLabel"
							aria-hidden="true">

							<div class="modal-dialog" role="document">
								<div class="modal-content">

									<!-- Add image inside the body of modal -->
									<div class="modal-body">
										<img id="image" alt="showing" src="">


									</div>

									<div class="modal-footer">
										<button type="button" class="btn btn-secondary"
											data-dismiss="modal">Close</button>
									</div>
								</div>
							</div>
						</div>


						<label class="form-label mb-3"> Employee ID/Employee Name</label><span
							class="red">*</span> <input type="text"
							class="form-control show-tick bootstrap-select2" id="eids"
							name="employeeId" autocomplete="false" list="eid"
							placeholder="Select Employee ID/Name "
							onkeyup="return removewaring()" />
						<datalist id="eid">
							<option value="ALL">ALL</option>
							<c:forEach items="${eid}" var="eid">
								<option value=${eid.e_id}-${eid.first_name}-${eid.last_name}></option>
							</c:forEach>
						</datalist>

						<div>
							<button style="margin-top: 6%;" class="btn btn-primary"
								type="submit" name="submit" onclick="myFunction()"
								value="submit">Submit</button>
						</div>
					</div>
				</div>
			</form>
			<div class="col-md-8" style="padding: 10px; min-height: 400px">
				<div class="table-responsive1" style="padding: 10px">
					<table id="ClientDetails"
						class="display nowrap table table-bordered table-striped table-hover"
						cellspacing="0" width="100%">
						<thead>
							<tr>
								<th>Title</th>
								<th>Message</th>
								<th>Sent from</th>
								<th>Sent to</th>
								<th>Status</th>
								<th>date</th>
								<th>Image</th>
							</tr>


						</thead>

						<tbody>
							<c:if test="${!empty notificationList}">
								<c:forEach items="${notificationList}" var="Listof">

									<tr>

										<td>${Listof.title}</td>
										<td>${Listof.message}</td>
										<td>${Listof.createdBy}</td>
										<td>${Listof.sendAll}</td>
										<td>${Listof.notifyStatus}</td>
										<td>${Listof.createdDate}</td>

										<td>

											<button type="button" class="btn btn-primary"
												data-toggle="modal" data-target="#exampleModal"
												onclick="showImage('${Listof.image}')">Show image</button>

										</td>
									</tr>
								</c:forEach>
							</c:if>
						</tbody>

					</table>
				</div>
			</div>
		</div>
	</div>

	<div></div>
	<div class="modal fade align-middle" id="sucessModel" role="dialog"
		data-keyboard="false" data-backdrop="static">

		<div class="modal-dialog  modal-md modal-dialog-centered">

			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">

					<h4 class="modal-title" align="center">Alert!</h4>
				</div>
				<div class="modal-body model-sm text-center">

					<b><span id="sucessModelMessage"></span></b>

				</div>
				<div class="modal-footer">

					<button type="button" class="btn btn-default" data-dismiss="modal">Ok</button>
				</div>
			</div>

		</div>
	</div>
	<script type="text/javascript">
		function showImage(image) {
			console.log(image);
			document.getElementById('image').src = image;
		}

		$('#ClientDetails').DataTable({
			"scrollY" : 280,
			"scrollX" : true,
			"pageLength" : 8,
			"dom" : 'lBfrtip',
		});

		$("").load(function() {
			console.log("Image loaded.");
			var message = "${message}";
			$("#sucessModelMessage").html(message);
			$("#sucessModel").modal('show');
		});
	</script>

</body>
</html>
