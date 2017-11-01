#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_diffuseTexture;
uniform sampler2D u_specularTexture;

uniform float u_usesDiffuseTexture;
uniform float u_usesSpecularTexture;

uniform vec4 u_globalAmbient;

uniform vec4 u_lightColor;

uniform vec4 u_spotDirection;
uniform float u_spotExponent;

uniform float u_constantAttenuation;
uniform float u_linearAttenuation;
uniform float u_quadraticAttenuation;

uniform vec4 u_lightDiffuse;
uniform vec4 u_materialDiffuse;
uniform vec4 u_materialSpecular;
uniform vec4 u_materialShininess;
uniform float u_shininessFactor;

uniform vec4 u_materialEmission;


varying vec4 v_s;
varying vec4 v_n;
varying vec2 v_uv;
varying vec4 v_h;

void main()
{
    vec4 materialDiffuse;
    if(u_usesDiffuseTexture == 1.0)
    {
        vec4 tmp = texture2D(u_diffuseTexture, v_uv);
        materialDiffuse = vec4(tmp.r, tmp.g, tmp.b, 1.0);
    }
    else
    {
        materialDiffuse = u_materialDiffuse;
    }

    vec4 materialSpecular;
    if(u_usesSpecularTexture == 1.0)
    {
        materialSpecular = texture2D(u_specularTexture, v_uv);
    }
    else
    {
        materialSpecular = u_materialSpecular;
    }

    // How light hits the objects
    float lampert = dot(v_n, v_s) / (length(v_n) * length(v_s));
        	lampert = ((lampert < 0.0) ? 0.0 : lampert);

    float phong = dot(v_n, v_h) / (length(v_n) * length(v_h));
    phong = (phong < 0.0 ? 0.0 : pow(phong, u_shininessFactor));

    vec4 color = (lampert * u_lightDiffuse * u_materialDiffuse);
    color += (phong * u_lightDiffuse * u_materialSpecular);


	gl_FragColor = color;
}