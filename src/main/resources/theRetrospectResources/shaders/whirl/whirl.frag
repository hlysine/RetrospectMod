#version 120

#ifdef GL_ES
precision mediump float;
#endif

// varying input variables from our vertex shader
varying vec4 v_color;
varying vec2 v_texCoords;

// a special uniform for textures
uniform sampler2D u_texture;

// size of current render target
uniform float res_x;
uniform float res_y;

// Swirl effect parameters
uniform float radius = 200.0;
uniform float angle = 0.8;
uniform vec2 center = vec2(400.0, 300.0);

vec4 PostFX(sampler2D tex, vec2 uv)
{
    vec2 texSize = vec2(res_x, res_y);
    vec2 tc = uv * texSize;
    tc -= center;
    float dist = length(tc);
    if (dist < radius)
    {
        float percent = (radius - dist) / radius;
        float theta = percent * percent * angle * 8.0;
        float s = sin(theta);
        float c = cos(theta);
        tc = vec2(dot(tc, vec2(c, -s)), dot(tc, vec2(s, c)));
    }
    tc += center;
    vec3 color = texture2D(u_texture, tc / texSize).rgb;
    return vec4(color, 1.0);
}

void main()
{
    gl_FragColor = v_color * PostFX(u_texture, v_texCoords);
}