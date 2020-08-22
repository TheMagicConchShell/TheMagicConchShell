import 'tui-color-picker/dist/tui-color-picker.css';
import colorSyntax from '@toast-ui/editor-plugin-color-syntax';

export default Object.freeze({
    options : {
        hooks:{
            addImageBlobHook :  async (blob, callback) => {
                const u = window.URL.createObjectURL(blob);
                const img = new Image();
                img.src = u;
                const canvas = document.createElement('canvas');
                img.onload=function(){
                    const MAX_WIDTH = 1000;
                    const MAX_HEIGHT = 800;
                    const width = img.width;
                    const height = img.height;
                        
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
                    const ctx = canvas.getContext('2d');
                    ctx.drawImage(img,0,0,width,height);
                    const ret = canvas.toDataURL();
                    callback(ret,"uploaded image");
                };
                
                return false;
            }
        },
        plugins: [colorSyntax],
    }
});