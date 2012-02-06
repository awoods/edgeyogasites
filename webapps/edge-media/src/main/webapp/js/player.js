function getStreamer(filename) {
    var so = new SWFObject('http://s3.amazonaws.com/aw-audio/player.swf', 'mpl',
                           '300', '24', '9');
    so.addParam('allowfullscreen', 'false');
    so.addParam('allowscriptaccess', 'always');
    so.addParam('wmode', 'opaque');
    so.addVariable('file', filename + '.mp3');
    so.addVariable('streamer', 'rtmp://shbgxgs8ge8i2.cloudfront.net/cfx/st/');
    return so;
    }