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

// frosted effect parameters
uniform float rnd_factor = 0.05;
uniform float rnd_scale = 5.1;
uniform vec2 v1 = vec2(92., 80.);
uniform vec2 v2 = vec2(41., 62.);

uniform vec2 focus = vec2(600, 300.0);
uniform float focus_radius = 100.0;
uniform float focus_radius_2 = 500.0;

float rand(vec2 co)
{
    return fract(sin(dot(co.xy, v1)) + cos(dot(co.xy, v2)) * rnd_scale);
}

void main()
{
    vec2 uv = v_texCoords;
    vec2 rnd = vec2(rand(uv.xy), rand(uv.yx));

    vec2 scaled_tc = uv * vec2(res_x, res_y);
    float clear_dist = length(scaled_tc - focus);
    float progress;
    if (clear_dist < focus_radius) {
        progress = 0;
    } else if (clear_dist < focus_radius_2) {
        progress = (clear_dist - focus_radius) / (focus_radius_2 - focus_radius);
    } else {
        progress = 1;
    }

    vec3 t_color = texture2D(u_texture, uv + rnd * rnd_factor * progress).rgb;
    gl_FragColor = v_color * vec4(t_color, 1.0);
}