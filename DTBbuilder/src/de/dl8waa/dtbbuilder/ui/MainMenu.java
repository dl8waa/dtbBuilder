package de.dl8waa.dtbbuilder.ui;

import static de.dl8waa.dtbbuilder.Constants.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import de.dl8waa.dtbbuilder.dtb.CRTCReader;
import de.dl8waa.dtbbuilder.dtb.DTB;
import de.dl8waa.dtbbuilder.dtb.FCCReader;


public class MainMenu {
	private Display display;
	private Shell shell;
	private Label statusLine;
	
	
	public MainMenu() {
		super();
		initShell();
		buildUI();
		runShell();
	}

	
	private void initShell() {
		display = Display.getDefault();
		shell = new Shell(display);
		shell.setText(APP_NAME);
		
		shell.setSize(240, 160);
		shell.setLayout(new FormLayout());
		
		// Catch Enter & Escape
        shell.addListener(SWT.Traverse, new Listener() {			
	        public void handleEvent(Event event) {	        	
	        	if (event.detail == SWT.TRAVERSE_RETURN) {
	        		event.doit = canRun();
	        		if (event.doit) { onRun(); }
	        		event.doit = false;
	        	}
	        	
	        	if (event.detail == SWT.TRAVERSE_ESCAPE) {
	        		if (onCancel() != SWT.CANCEL) {
	        			shell.dispose();
	        		}
	        	}
	        }
	    });

        // Catch dialog close
     	shell.addListener(SWT.Close, new Listener() {
     		@Override public void handleEvent(Event event) {
     			event.doit = onCancel() != SWT.CANCEL;
     		}
     	});
	}
	
	
	private void buildUI() {
		final int BUTTON_WIDTH = 80;
		final int BUTTON_HEIGHT = 40;
		
		Button bGoAhead = new Button(shell, SWT.PUSH);
		bGoAhead.setText("Go");
		bGoAhead.addSelectionListener(new SelectionListener() {
			@Override public void widgetSelected(SelectionEvent e) { onRun(); }
			@Override public void widgetDefaultSelected(SelectionEvent e) { widgetSelected(e); }
		});
		FormData formData = new FormData(BUTTON_WIDTH, BUTTON_HEIGHT);
		formData.top = new FormAttachment(50, -(BUTTON_HEIGHT/2+15));
		formData.left = new FormAttachment(50, -BUTTON_WIDTH/2);
		bGoAhead.setLayoutData(formData);
		
		statusLine = new  Label(shell, SWT.CENTER);
		statusLine.setText(APP_REVISION);
		formData = new FormData();
		formData.top = new FormAttachment(bGoAhead, 15);
		formData.left = new FormAttachment(0, 10);
		formData.right = new FormAttachment(100, -10);
		statusLine.setLayoutData(formData);
	}

	
	private void runShell() {
		shell.open();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}
	
	
	private boolean canRun() {
		return true;
	}
	
	
	private RunResult onRun() {
		FileSelectionDialog fileSelectionDialog = new FileSelectionDialog(shell);
		if (!fileSelectionDialog.hasData()) {
			return RunResult.NO_FILES;
		}
		
		statusLine.setText("Read " + fileSelectionDialog.getFccDbFilenameWithoutPath() + " ...");
		FCCReader fccReader = new FCCReader(fileSelectionDialog.getFccDbFilename());
		
		statusLine.setText("Read " + fileSelectionDialog.getCrtcDbFilenameWithoutPath() + " ...");
		CRTCReader crtcReader = new CRTCReader(fileSelectionDialog.getCrtcDbFilename());
		
		if (!fccReader.getReadSuccess() || !crtcReader.getReadSuccess()) {
			statusLine.setText("Read error.");
			return RunResult.READ_ERROR;
		}
		
		DTB dtb = new DTB();
		dtb.addList(fccReader.getDTB());
		dtb.addList(crtcReader.getDTB());
		statusLine.setText("Write " + fileSelectionDialog.getOutputFilenameWithoutPath());
		if (!dtb.write(fileSelectionDialog.getOutputFilename())) {
			statusLine.setText("Write error.");
			return RunResult.WRITE_ERROR;
		}
		
		statusLine.setText("Successfully done.");
		return RunResult.SUCCESS;
	}
	
	
	private int onCancel() {
		return SWT.OK;
	}

}
