import 'tui-color-picker/dist/tui-color-picker.css';
import colorSyntax from '@toast-ui/editor-plugin-color-syntax';

export default Object.freeze({
    options : {
        hooks:{
            addImageBlobHook :  async (blob, callback)=>{
                var u = window.URL.createObjectURL(blob);
                var img = new Image();
                img.src = u;
                var canvas = document.createElement('canvas');
                img.onload=function(){
                    var MAX_WIDTH = 1000;
                    var MAX_HEIGHT = 800;
                    var width = img.width;
                    var height = img.height;
                        
                    if (width > height) {
                        if (width > MAX_WIDTH) {
                            height *= MAX_WIDTH / width;
                            width = MAX_WIDTH;
                        }
                    } else {
                        if (height > MAX_HEIGHT) {
                            width *= MAX_HEIGHT / height;
                            height = MAX_HEIGHT;
                        }
                    }
                    canvas.width = width;
                    canvas.height = height;
                    var ctx = canvas.getContext('2d');
                    ctx.drawImage(img,0,0,width,height);
                    var ret = canvas.toDataURL();
                    callback(ret,"uploaded image");
                };
                
                return false;
            }
        },
        plugins: [colorSyntax],
    }
});