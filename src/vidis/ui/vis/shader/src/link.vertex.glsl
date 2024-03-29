//	VIDIS is a simulation and visualisation framework for distributed systems.
//	Copyright (C) 2009 Dominik Psenner, Christoph Caks
//	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
//	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
//	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.

// uniform qualified variables are changed at most once per primitive

// attribute qualified variables ar typically changed per vertex
attribute vec3 packet0;
attribute vec3 packet1;
attribute vec3 packet2;
attribute vec3 packet3;
attribute vec3 packet4;
attribute vec3 packet5;
attribute vec3 packet6;
attribute vec3 packet7;
attribute vec3 packet8;
attribute vec3 packet9;



// varying qualified variables communicate from the vertex shader to
// the framgment shader
varying vec3 normal;
varying vec3 incom;

vec3 maximizeAddVec( vec3 vec );


float functioner(float distance) {
	return (distance + (1.0/distance)*0.014);
}

vec3 recalculateVertex(vec3 packet, vec3 vertex, float distance) {
	return packet + functioner(distance) * normalize (vertex - packet);
}

vec3 calculateAddVec( vec3 dir ) {
	float l = length( dir );
	if ( l <= 1.5 ) {
		return functioner( l ) * normalize( dir ) - dir;
	}
	else {
		return vec3( 0, 0, 0 );
	}
}

//vec3 maximizeAddVec( vec3 vec ) {
//	float maxLength = 0.1;
//	vec3 vecr;
//	if ( length ( vec ) > maxLength ) {
//		vecr = normalize ( vec ) * maxLength;
//	}
//	else {
//		vecr = vec;
//	}
//	return vecr;
//}



void main() {
	
	vec3 newVertex;
	vec3 oldVertex = vec3(gl_Vertex);
	float l = length(oldVertex - packet1);
	
	newVertex = oldVertex;
	newVertex += calculateAddVec( oldVertex - packet0 );
	newVertex += calculateAddVec( oldVertex - packet1 );
	newVertex += calculateAddVec( oldVertex - packet2 );
	newVertex += calculateAddVec( oldVertex - packet3 );
	newVertex += calculateAddVec( oldVertex - packet4 );
	newVertex += calculateAddVec( oldVertex - packet5 );
	newVertex += calculateAddVec( oldVertex - packet6 );
	newVertex += calculateAddVec( oldVertex - packet7 );
	newVertex += calculateAddVec( oldVertex - packet8 );
	newVertex += calculateAddVec( oldVertex - packet9 );
	
	incom = newVertex - vec3(0, 10, 0);
	normal = gl_NormalMatrix * gl_Normal;
	
	
	gl_Position = gl_ModelViewProjectionMatrix * vec4(newVertex, gl_Vertex.w);
	gl_FrontColor = gl_Color;
}