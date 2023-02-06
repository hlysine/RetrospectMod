#version 120

#ifdef GL_ES
precision mediump float;
#endif

// varying input variables from our vertex shader
varying vec4 v_color;
varying vec2 v_texCoords;

// a special uniform for textures
uniform sampler2D u_texture;

uniform vec2 center; // Mouse position
uniform float time; // effect elapsed time
uniform vec3 shockParams = vec3(10.0, 0.8, 0.1);

void main()
{
    vec2 uv = v_texCoords;
    vec2 texCoord = uv;
    float distance = distance(uv, center);
    if ((distance <= (time + shockParams.z)) && (distance >= (time - shockParams.z)))
    {
        float diff = (distance - time);
        float powDiff = 1.0 - pow(abs(diff * shockParams.x), shockParams.y);
        float diffTime = diff * powDiff;
        vec2 diffUV = normalize(uv - center);
        texCoord = uv + (diffUV * diffTime);
    }
    gl_FragColor = v_color * texture2D(u_texture, texCoord);
}