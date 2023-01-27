#ifdef GL_ES
precision mediump float;
#endif

// varying input variables from our vertex shader
varying vec4 v_color;
varying vec2 v_texCoords;

// a special uniform for textures
uniform sampler2D u_texture;

void main()
{
    // set the colour for this fragment|pixel
    vec4 texColor = texture2D(u_texture, v_texCoords);
    gl_FragColor = v_color * vec4((1.0 - texColor).rgb, texColor.a);
}