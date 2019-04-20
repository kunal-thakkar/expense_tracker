var gulp = require('gulp');
var concat = require('gulp-concat');

gulp.task('scripts', function() {
	return gulp.src([
	    'public/js/jquery-3.3.1.min.js',
		'public/js/flot/jquery.canvaswrapper.js',
		'public/js/flot/jquery.colorhelpers.js',
		'public/js/flot/jquery.flot.js',
		'public/js/flot/jquery.flot.saturated.js',
		'public/js/flot/jquery.flot.browser.js',
		'public/js/flot/jquery.flot.drawSeries.js',
		'public/js/flot/jquery.flot.errorbars.js',
		'public/js/flot/jquery.flot.uiConstants.js',
		'public/js/flot/jquery.flot.logaxis.js',
		'public/js/flot/jquery.flot.symbol.js',
		'public/js/flot/jquery.flot.flatdata.js',
		'public/js/flot/jquery.flot.navigate.js',
		'public/js/flot/jquery.flot.fillbetween.js',
		'public/js/flot/jquery.flot.stack.js',
		'public/js/flot/jquery.flot.touchNavigate.js',
		'public/js/flot/jquery.flot.hover.js',
		'public/js/flot/jquery.flot.touch.js',
		'public/js/flot/jquery.flot.time.js',
		'public/js/flot/jquery.flot.axislabels.js',
		'public/js/flot/jquery.flot.selection.js',
		'public/js/flot/jquery.flot.composeImages.js',
		'public/js/flot/jquery.flot.legend.js',
		'public/js/flot/jquery.flot.pie.js',
		// 'public/js/moment.js',
		'public/js/jquery.dataTables.min.js',
		'public/js/dataTables.responsive.min.js',
		'public/js/angular.min.js',
		'public/js/angular-route.js',
		'public/js/main.js',
		'public/js/util.js',
		'public/js/directive.js',
		'public/js/dashboard.js',
		'public/js/transaction.js'		
	])
    .pipe(concat('app.js'))
    .pipe(gulp.dest('./src/main/resources/static/js'));
});

gulp.task('minify-css', function(){
	return gulp.src([
		'public/css/jquery.dataTables.min.css',
		'public/css/responsive.dataTables.min.css',
		'public/css/styles.css'
	])
	.pipe(concat("style.css"))
	.pipe(gulp.dest('./src/main/resources/static/css'));
})

gulp.task('default', function () {
	return gulp.watch('public/**/*.*', gulp.parallel('scripts', 'minify-css'));
});