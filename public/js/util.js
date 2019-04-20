app.factory('AppUtil', function(){
    return {
        addDays: function(date, days) {
            var result = new Date(date);
            result.setDate(result.getDate() + days);
            return result;
        },
        formatCurrency: function(n){
            return '&#8377;' + " " + Number(Math.round(n + 'e2') + 'e-2').toFixed(2);
        },
        formatDate: function(date){
            var m = date.getMonth()+1;
            return date.getFullYear() + "-" + (m<=9?"0":"") + m + "-" + date.getDate();
        },
        getColor: function(){
            var r = Math.floor(Math.random() * 255);
            var g = Math.floor(Math.random() * 255);
            var b = Math.floor(Math.random() * 255);
            var decColor =0x1000000+ b + 0x100 * g + 0x10000 *r;
            return '#'+decColor.toString(16).substr(1);
        }
    }
});