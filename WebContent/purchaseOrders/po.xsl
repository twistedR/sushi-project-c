<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<head>
				<title>Order for <xsl:value-of select="/order/customer/name"/></title>
				<link href="../../assets/css/bootstrap.css" rel="stylesheet"/>
				<link href="../../assets/css/bootstrap-theme.css" rel="stylesheet"/>
				<link href="../../assets/css/style.css" rel="stylesheet"/>
				<style>
					#main{
						margin-top: 2em;
					}
				</style>
			</head>
			<body>
				<div class="container" id="main">
					<div class="row">
						<div class="col-md-2">
							<img class="img-responsive" src="../../assets/images/main_logo_500.png"/>
						</div>
						<div class="col-md-5 col-md-offset-1">
							<h1>Order Details</h1>
							<table class="table no-border">
								<tr>
									<td class="bold">Order Id</td>
									<td><xsl:value-of select="order/@id"/></td>
								</tr>
								<tr>
									<td class="bold">Customer Name</td>
									<td><xsl:value-of select="order/customer/name"/></td>
								</tr>
								<tr>
									<td class="bold">Customer Number</td>
									<td><xsl:value-of select="order/customer/@number"/></td>
								</tr>
							</table>
						</div>
					</div>
					<div class="row">
						<table class="table table-striped table-responsive table-bordered">
							<thead>
								<tr>
									<td>Name</td>
									<td>Price</td>
									<td>Quantity</td>
									<td>Total</td>
								</tr>
							</thead>
							<xsl:apply-templates select="order/items/item"/>
						</table>
					</div>
					<div class="row">
						<div class="col-md-5 col-md-offset-7">
						<table class="table table-bordered">
							<tr>
								<td class="bold">Subtotal</td>
								<td>$<xsl:value-of select="format-number(order/total, '#.00')"/></td>
							</tr>
							
							<tr>
								<td class="bold">Shipping</td>
								<td>$<xsl:value-of select="format-number(order/shipping, '#.00')"/></td>
							</tr>
							
							<tr>
								<td class="bold">HST</td>
								<td>$<xsl:value-of select="format-number(order/HST, '#.00')"/></td>
							</tr>
							
							<tr>
								<td class="bold">Grand Total</td>
								<td>$<xsl:value-of select="format-number(order/grandTotal, '#.00')"/></td>
							</tr>
							
						</table>
					</div>
					</div>
				</div>
				<div class="col-xs-12 footer-row">
					<div class="row">
						<div class="col-md-4 col-md-offset-3">
							<p>Got a question?
								<span class="glyphicon glyphicon-envelope">
									<a href="mailto:info@foodsrus.ca">info@foodsrus.ca</a>
								</span>
							</p>
						</div>
						<div class="col-md-2">
							<p>
								&#169; Foods 'R' Us <em>2013</em>
							</p>
						</div>
					</div>
				</div>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="items/item">
		<tr>
			<td><xsl:value-of select="./name"/></td>
			<td>$<xsl:value-of select="format-number(./price, '#.00')"/></td>
			<td><xsl:value-of select="./quantity"/></td>
			<td>$<xsl:value-of select="format-number(./extended, '#.00')"/></td>
		</tr>
	</xsl:template>
</xsl:stylesheet>