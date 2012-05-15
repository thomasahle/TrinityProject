package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Font;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.TypedEvent;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.Pointer;
import playn.core.Pointer.Event;
import playn.core.TextFormat;
import playn.core.TextFormat.Alignment;
import playn.core.TextLayout;

import com.github.thomasahle.trainbox.trainbox.core.TrainBox;
import com.github.thomasahle.trainbox.trainbox.model.Level;
import com.github.thomasahle.trainbox.trainbox.util.LevelTracker;

public class LevelSelectScene implements Scene, Keyboard.Listener {
	
	private final TrainBox trainBox;
	int width = graphics().width();
	int height = graphics().height();
	CanvasImage bgImage = graphics().createImage(graphics().width(),graphics().height());
    ImageLayer bgLayer;
    GroupLayer demoLayer;
    final ImageLayer demoPageImageLayer;
	private GroupLayer demoLayerLevels;
	private boolean tPressed;
	
	public LevelSelectScene(final TrainBox trainBox ){
		this.trainBox = trainBox;
		
		bgLayer = graphics().createImageLayer(bgImage);
		Canvas canvas = bgImage.canvas();
        final Image backgroundImage = assets().getImage("images/pngs/standardBackground.png");
		canvas.drawImage(backgroundImage, 0, 0);
		
		
		// Create the demoLayer that contains the level select pages
        demoLayer = graphics().createGroupLayer();
        demoLayer.setTranslation(width/20+40, height/20);
		
        final Image demoPageImage = assets().getImage("images/pngs/chooseLevelBlurb.png");
        demoPageImageLayer = graphics().createImageLayer(demoPageImage);
    	demoLayer.add(demoPageImageLayer);
	}

	private void initializeLevelButtons() {
		demoLayerLevels = graphics().createGroupLayer();
		int numberOfLevels = Level.levels.size();
		int currentProgress = LevelTracker.getCurrentProgress();

		float x = 90;
		float y = 200;
		int j = 0;
		final Image levelButtonOk = assets().getImage(
				"images/pngs/levelButton.png");
		final Image levelButtonNotOk = assets().getImage(
				"images/pngs/inaccessibleLevelButton.png");
		final Image levelButtonActive = assets().getImage(
				"images/pngs/levelButtonActive.png");
		
		for (int i = 0; i < numberOfLevels; i++) {
			Layer levelButton;
			if (i <= currentProgress) {
				levelButton = createPlayableButton(levelButtonOk, levelButtonActive, i);
			} else {
				levelButton = createUnPlayableButton(levelButtonNotOk);
				final int level = i;
				levelButton.addListener(new Pointer.Adapter() {
					@Override
					public void onPointerStart(Event event) {
						if (tPressed)
							trainBox.setLevel(level);
					}
				});
			}
			demoLayerLevels.add(levelButton);
			levelButton.setTranslation(x, y);
			j += 1;
			x += levelButtonOk.width() + 10;
			if (j == 6) {
				x = 90;
				y += levelButtonOk.height() + 20;
				j = 0;
			}
		}

		demoLayer.add(demoLayerLevels);

	}

	private Layer createUnPlayableButton(Image levelButtonNotOk) {
		return graphics().createImageLayer(levelButtonNotOk);
	}

	private Layer createPlayableButton(Image image,
			final Image activeImage, final int level) {
		GroupLayer glayer = graphics().createGroupLayer();
		final ImageLayer ilayer = graphics().createImageLayer(image);
		glayer.add(ilayer);
		
		CanvasImage textImage = graphics().createImage(image.width(), image.height());
		Font font = graphics().createFont("Sans", Font.Style.BOLD, 16);
		TextFormat format = new TextFormat().withFont(font)
				.withEffect(TextFormat.Effect.outline(0x887f501d))
				.withTextColor(0xffca7829)
				.withAlignment(Alignment.RIGHT);
		TextLayout layout = graphics().layoutText(""+level, format);
		if (level <= 9)
			textImage.canvas().drawText(layout, 80, 17);
		else textImage.canvas().drawText(layout, 75, 17);
		Layer tlayer = graphics().createImageLayer(textImage);
		glayer.add(tlayer);
		
		tlayer.addListener(new Pointer.Adapter() {
			@Override
			public void onPointerStart(Event event) {
				ilayer.setImage(activeImage);
			}
			@Override
			public void onPointerEnd(Event event) {
				trainBox.setLevel(level);
			}
		});
		
		return glayer;
	}
	
	@Override
	public void update(float delta) {}

	@Override
	public void onAttach() {
    	initializeLevelButtons();
		graphics().rootLayer().add(bgLayer);
	    graphics().rootLayer().add(demoLayer);
	    PlayN.keyboard().setListener(this);
	}

	@Override
	public void onDetach() {
		demoLayer.remove(demoLayerLevels);
		demoLayerLevels.destroy();
		graphics().rootLayer().remove(bgLayer);
	    graphics().rootLayer().remove(demoLayer);
	    PlayN.keyboard().setListener(null);
	}

	@Override
	public void onKeyDown(playn.core.Keyboard.Event event) {
		tPressed = event.key() == Key.T;
	}
	@Override
	public void onKeyTyped(TypedEvent event) {}
	@Override
	public void onKeyUp(playn.core.Keyboard.Event event) {
		tPressed = false;
	}

}
