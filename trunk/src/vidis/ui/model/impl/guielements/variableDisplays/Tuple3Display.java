package vidis.ui.model.impl.guielements.variableDisplays;

import java.awt.Color;

import javax.media.opengl.GL;
import javax.vecmath.Tuple3b;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple3i;

import org.apache.log4j.Logger;

import vidis.data.var.AVariable;

public class Tuple3Display extends Display {
	private static Logger logger = Logger.getLogger(Tuple3Display.class);

	public Tuple3Display ( AVariable v ) {
		super(v);
		this.setText( "Label" );
	}
	
//	@Override
//	public Display newInstance( AVariable var ) {
//		return new Tuple3Display( var );
//	}
	
	private String convertUnknownTupleToString() {
		Object tuple = var.getData();
		if ( tuple instanceof Tuple3d ) {
			return ((Tuple3d)tuple).toString();
		} else if ( tuple instanceof Tuple3b ) {
			return ((Tuple3b)tuple).toString();
		} else if ( tuple instanceof Tuple3f ) {
			return ((Tuple3f)tuple).toString();
		} else if ( tuple instanceof Tuple3i ) {
			return ((Tuple3i)tuple).toString();
		} else {
			return "ERROR";
		}
	}

	
	
	@Override
	public void renderContainer(GL gl) {
		String txt = var.getIdentifier().replaceAll(var.getNameSpace()+".", "   ") + " [T]-> " + convertUnknownTupleToString();
		this.setText(txt);
		super.renderContainer(gl);
	}
//	@Override
//	public void renderContainer(GL gl) {
//		String txt = var.getIdentifier() + " -> " + var.getData().toString();
//		double scale = 0.01;
//		height = textH * scale;
//		double hoffset = height / 2d;
//		gl.glPushMatrix();
//			gl.glTranslated(0, 0, 0);
//			gl.glScaled( scale, scale, scale );
//			textRenderer.begin3DRendering();
//			textRenderer.setUseVertexArrays(false);
//			textRenderer.draw3D( txt, 0f, 0f, 0f, 1f );
//			textRenderer.end3DRendering();
//		gl.glPopMatrix();
//		//super.renderContainer(gl);
//	}
}