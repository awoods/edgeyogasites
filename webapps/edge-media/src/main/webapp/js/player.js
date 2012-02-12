function getStreamer(filename) {
    var so = new SWFObject('http://edgeyoga.com/js/player.swf', 'mpl',
                           '300', '24', '9');
    so.addParam('allowfullscreen', 'false');
    so.addParam('allowscriptaccess', 'always');
    so.addParam('wmode', 'opaque');
    so.addVariable('file', filename + '.mp3');
    so.addVariable('streamer', 'rtmp://s8pv6i3s5wlxz.cloudfront.net/cfx/st/');
    return so;
    }