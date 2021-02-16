import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.AffineTransform
import java.awt.geom.Path2D
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import java.util.*
import javax.swing.JPanel
import kotlin.math.cos
import kotlin.math.sin

class Viewport : JPanel() {
    private var cursor = Point2D.Double()
    private val shapes = mutableListOf<Shape>()
    private val circle = createCircle(60.0, 48)

    @Override
    override fun paintComponent(g: Graphics) {
        g as Graphics2D
        g.background = background
        g.clearRect(0, 0, width, height)
        g.color = Color(0x547581)
        drawCell(g, Rectangle2D.Double(0.0, 0.0, width - 1.0, height - 1.0), shapes, 8)
        g.color = Color.GREEN
        shapes.forEach { g.draw(it) }
    }

    private fun drawCell(g: Graphics2D, cell: Rectangle2D.Double, shapes: List<Shape>, level: Int) {
        if (level == 0) return
        g.draw(cell)
        val children = shapes.filter { it.intersects(cell) }.toList()
        if (children.size < 2) return
        val center = Point2D.Double(cell.width / 2.0, cell.height / 2.0)
        drawCell(g, Rectangle2D.Double(cell.x, cell.y, center.x, center.y), shapes, level - 1)
        drawCell(g, Rectangle2D.Double(cell.x + center.x, cell.y, center.x, center.y), shapes, level - 1)
        drawCell(g, Rectangle2D.Double(cell.x, cell.y + center.y, center.x, center.y), shapes, level - 1)
        drawCell(g, Rectangle2D.Double(cell.x + center.x, cell.y + center.y, center.x, center.y), shapes, level - 1)
    }

    private fun createCircle(radius: Double, segments: Int) = createShape(*(0 until segments).map {
        val arc = 2.0 * Math.PI * it / segments
        val cos = cos(arc) * radius
        val sin = sin(arc) * radius
        Point2D.Double(cos - sin, cos + sin)
    }.toTypedArray())

    private fun createShape(vararg points: Point2D.Double) = Path2D.Double().apply {
        moveTo(points.first().x, points.first().y)
        Arrays.stream(points).skip(1).forEach { lineTo(it.x, it.y) }
        closePath()
    }

    init {
        background = Color(0x344550)
        size = Dimension(1000, 800)
        preferredSize = size

        shapes.add(circle)
        shapes.add(
            createShape(
                Point2D.Double(600.0, 370.0),
                Point2D.Double(760.0, 690.0),
                Point2D.Double(440.0, 690.0)
            )
        )
        shapes.add(
            createShape(
                Point2D.Double(146.0, 234.0),
                Point2D.Double(346.0, 234.0),
                Point2D.Double(346.0, 434.0),
                Point2D.Double(146.0, 434.0)
            )
        )

        addMouseMotionListener(object : MouseAdapter() {
            @Override
            override fun mouseMoved(e: MouseEvent) {
                circle.transform(AffineTransform(1.0, 0.0, 0.0, 1.0, e.x - cursor.x, e.y - cursor.y))
                cursor.setLocation(e.x.toDouble(), e.y.toDouble())
                repaint()
            }
        })
    }
}
