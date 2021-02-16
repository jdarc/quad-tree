import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.SwingUtilities

object Program {
    @JvmStatic
    fun main(args: Array<String>) {
        SwingUtilities.invokeLater {
            val frame = JFrame("Quad Tree")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.layout = BorderLayout()
            frame.contentPane.add(Viewport(), BorderLayout.CENTER)
            frame.pack()
            frame.isResizable = false
            frame.isVisible = true
            frame.setLocationRelativeTo(null)
        }
    }
}
