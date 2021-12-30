$(function(){
    $(".cell.cnt").find(".total").text($(".slide").length);
    Slider1__init();
})
var windowFocusHere = true;

// 다른 탭으로 이동했을 때
$(window).on("blur", function() {
    windowFocusHere = true;
});

// 다시 해당 윈도우(브라우저로 돌아왔을 때)
$(window).on("focus", function() {
    windowFocusHere = true;
});


function Slider1__init() {
    $('.btn-stop').click(function() {
        var $slider = $(this).closest('.slider-1');
        $slider.attr('data-slider-1-autoplay-status', 'N');
    });
    
    $('.btn-play').click(function() {
        var $slider = $(this).closest('.slider-1');
        $slider.attr('data-slider-1-autoplay-status', 'Y');
    });
    
    Slider1__update();
}

var Slider1__updateLastTimestamp = 0;

function Slider1__movePrev($slider) {
    var $current = $slider.find('> .slides > .active');
    var $post = $current.prev();
    
    if ( $post.length == 0 ) {
        $post = $slider.find('> .slides > :last-child');
    }
    
    $current.removeClass('active');
    $post.addClass('active');
    $(".cell.cnt").find(".cur").text($post.index() + 1);
}

function Slider1__moveNext($slider) {
    var $current = $slider.find('> .slides > .active');
    var $post = $current.next();
    
    if ( $post.length == 0 ) {
        $post = $slider.find('> .slides > :first-child');
    }
    
    $current.removeClass('active');
    $post.addClass('active');
    $(".cell.cnt").find(".cur").text($post.index() + 1);
}

function Slider1__update(timestamp, fn) {
    if ( !timestamp ) {
        timestamp = 0;
    }
    
    var delta = timestamp - Slider1__updateLastTimestamp;
    
    $('.slider-1').each(function(index, node) {
        var $slider = $(this);
        
        var $progressBarGage = $slider.find(' > .nav-bar .progress-bar > div');
        
        var autoplayTimeout = parseInt($slider.attr('data-slider-1-autoplay-timeout'));
        var autoplayCurrent = parseInt($slider.attr('data-slider-1-autoplay-current'));
        var autoplayStatus = $slider.attr('data-slider-1-autoplay-status') !== 'N';
        
        if ( autoplayStatus && windowFocusHere ) {
            autoplayCurrent += delta;
        
            if ( autoplayCurrent > autoplayTimeout ) {
                Slider1__moveNext($slider);
                
                autoplayCurrent = 0;
            }
            
            var percent = autoplayCurrent / autoplayTimeout * 100;
            
            $progressBarGage.css('width', percent + '%');

            $slider.attr('data-slider-1-autoplay-current', autoplayCurrent)
        }
    });
    
    Slider1__updateLastTimestamp = timestamp;
    
    requestAnimationFrame(Slider1__update);
}

function fn_onclickPrevBtn() {
    Slider1__movePrev($(".slider-1"));
}

function fn_onclickNextBtn() {
    Slider1__moveNext($(".slider-1"));
}

