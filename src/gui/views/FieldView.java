package gui.views;

import gui.*;
import gui.components.MovableComponent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Graphic user interface of the game's main view
 *
 * @author Ignacio Slater Muñoz
 * @version 3.0b7
 * @since 3.0
 */
public class FieldView extends JPanel {

  /** Each cell is a 128x128 square */
  private final int CELL_SIZE = 128;

  private ArrayList<Wall> walls;
  private ArrayList<Baggage> baggs;
  private ArrayList<Area> areas;

  private MovableComponent sprite;
  private int fieldWidth = 0;
  private int fieldHeigth = 0;

  /**
   * Creates the view for the game field.
   */
  public FieldView() {
    setupKeyMappings();
    setFocusable(true);
    initField();
  }

  /**
   * Defines the actions realized when a key is pressed
   */
  private void setupKeyMappings() {
    addActionMapping(KeyStroke.getKeyStroke(Keys.DOWN), new MoveSpriteDownAction());
    addActionMapping(KeyStroke.getKeyStroke(Keys.UP), new MoveSpriteUpAction());
    addActionMapping(KeyStroke.getKeyStroke(Keys.RIGHT), new MoveSpriteRightAction());
    addActionMapping(KeyStroke.getKeyStroke(Keys.LEFT), new MoveSpriteLeftAction());
  }

  /**
   * Initializes the field
   */
  private void initField() {
    walls = new ArrayList<>();
    baggs = new ArrayList<>();
    areas = new ArrayList<>();

    final int OFFSET = 10;
    int x = OFFSET;
    int y = OFFSET;

    Wall wall;
    Baggage b;
    Area a;

    final String level = "    ######\n"
        + "@    ##   #\n"
        + "    ##$  #\n"
        + "  ####  $##\n"
        + "  ##  $ $ #\n"
        + "#### # ## #   ######\n"
        + "##   # ## #####  ..#\n"
        + "## $  $          ..#\n"
        + "###### ### # ##  ..#\n"
        + "    ##     #########\n"
        + "    ########\n";

    for (int i = 0; i < level.length(); i++) {
      char item = level.charAt(i);
      switch (item) {
        case '\n':
          y += CELL_SIZE;
          if (this.fieldWidth < x) {
            this.fieldWidth = x;
          }
          x = OFFSET;
          break;
        case '#':
          wall = new Wall(x, y);
          walls.add(wall);
          x += CELL_SIZE;
          break;
        case '$':
          b = new Baggage(x, y);
          baggs.add(b);
          x += CELL_SIZE;
          break;
        case '.':
          a = new Area(x, y);
          areas.add(a);
          x += CELL_SIZE;
          break;
        case '@':
          sprite = new Player(x, y);
          x += CELL_SIZE;
          break;
        case ' ':
          x += CELL_SIZE;
          break;
        default:
          break;
      }
      fieldHeigth = y;
    }
  }

  /**
   * Associates a key stroke with an action.
   *
   * @param keyStroke
   *     the pressed key
   * @param action
   *     the action to be performed
   */
  private void addActionMapping(KeyStroke keyStroke, Action action) {
    this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, action.hashCode());
    this.getActionMap().put(action.hashCode(), action);
  }

  /**
   * @return the field's width (in pixels)
   */
  public int getFieldWidth() {
    return this.fieldWidth;
  }

  /**
   * @return the field's height (in pixels)
   */
  public int getFieldHeight() {
    return this.fieldHeigth;
  }

  @Override
  public void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    buildField(graphics);
  }

  /**
   * Builds the field according to the map generated by the controller
   *
   * @param graphics
   *     the object that's going to be displayed
   */
  private void buildField(Graphics graphics) {
    graphics.setColor(new Color(250, 240, 170));
    graphics.fillRect(0, 0, this.getWidth(), this.getHeight());

    ArrayList<MovableComponent> field = new ArrayList<>();

    field.addAll(walls);
    field.addAll(areas);
    field.addAll(baggs);
    field.add(sprite);

    for (MovableComponent item : field) {
      if (item instanceof Player || item instanceof Baggage) {
        graphics.drawImage(item.getImage(), item.x() + 2, item.y() + 2, this);
      } else {
        graphics.drawImage(item.getImage(), item.x(), item.y(), this);
      }
      final boolean isCompleted = false;
      if (isCompleted) {
        graphics.setColor(new Color(0, 0, 0));
        graphics.drawString("Completed", 25, 20);
      }
    }
  }

  private abstract class AbstractMoveSpriteAction extends AbstractAction {

    protected void moveSprite(int horizontalMovement, int verticalMovement) {
      sprite.move(horizontalMovement, verticalMovement);
      FieldView.this.repaint();
    }
  }

  private final class MoveSpriteDownAction extends AbstractMoveSpriteAction {

    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
      moveSprite(0, CELL_SIZE);
    }
  }

  private final class MoveSpriteUpAction extends AbstractMoveSpriteAction {

    @Override
    public void actionPerformed(final ActionEvent e) {
      moveSprite(0, -CELL_SIZE);
    }
  }

  private final class MoveSpriteRightAction extends AbstractMoveSpriteAction {

    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
      moveSprite(CELL_SIZE, 0);
    }
  }

  private final class MoveSpriteLeftAction extends AbstractMoveSpriteAction {

    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
      moveSprite(-CELL_SIZE, 0);
    }
  }
}