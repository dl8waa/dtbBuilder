package de.dl8waa.dtbbuilder.ui;

import static de.dl8waa.dtbbuilder.Constants.APP_REVISION;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.dl8waa.dtbbuilder.Constants.FileAction;
import de.dl8waa.dtbbuilder.utils.Utils;


public class FileSelectionDialog extends Dialog {
	private Display display;
	private Shell shell;
	private Label statusLine;
	private Text txFCC_database, txCRTC_database, txOutputFilename;
	private boolean disposeDisplay;
	
	private String fccDbFilename;
	public String getFccDbFilename() { return fccDbFilename; }
	public String getFccDbFilenameWithoutPath() { return Utils.extractFileName(fccDbFilename); }

	private String crtcDbFilename;	
	public String getCrtcDbFilename() { return crtcDbFilename; }
	public String getCrtcDbFilenameWithoutPath() { return Utils.extractFileName(crtcDbFilename); }
	
	private String outputFilename;
	public String getOutputFilename() { return outputFilename; }
	public String getOutputFilenameWithoutPath() { return Utils.extractFileName(outputFilename); }

	public FileSelectionDialog(Shell parent) {
		super(parent);
		
		initUI();
		buildUi();
		showUI();
	}
	
	private void initUI() {				
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE | SWT.PRIMARY_MODAL);
		shell.setText("Select database files");
		shell.setSize(600, 420);
        shell.setLayout(new FormLayout());
        
        display = shell.getDisplay();
		if (display == null) {
			disposeDisplay = true;
			display = new Display();
		}
		
        Utils.centerShell(display, shell);
        
        // Catch Enter
        shell.addListener(SWT.Traverse, new Listener() {			
	        public void handleEvent(Event e) {	        	
	        	if (e.detail == SWT.TRAVERSE_RETURN) {
	        		e.doit = canRun();
	        		if (e.doit) { onRun(); }
	        	}
	        }
	    });

        // Catch dialog close
     	shell.addListener(SWT.Close, new Listener() {
     		@Override public void handleEvent(Event event) {
     			fccDbFilename = "";
     			crtcDbFilename = "";
     			outputFilename = "";
     		}
     	});
	}
	
	
	private void buildUi() {
		final int MARGIN = 20;
		FormData formData;
		
		// Create a status line
		statusLine = new Label(shell, SWT.BORDER);
		statusLine.setText(APP_REVISION);
		formData = new FormData();
		formData.left = new FormAttachment(0);
		formData.right = new FormAttachment(100);
		formData.bottom = new FormAttachment(100);
		statusLine.setLayoutData(formData);
		
		Button bGoAhead = new Button(shell, SWT.PUSH);
		bGoAhead.setText("Ok");
		bGoAhead.addSelectionListener(new SelectionListener() {
			@Override public void widgetSelected(SelectionEvent e) { onRun(); }
			@Override public void widgetDefaultSelected(SelectionEvent e) { widgetSelected(e); }
		});
		formData = new FormData(75, 26);
		formData.top = new FormAttachment(100, -70);
		formData.left = new FormAttachment(100, -100);
		bGoAhead.setLayoutData(formData);
		
		Group gbInputFiles = new Group(shell, SWT.SHADOW_IN);
        gbInputFiles.setText("Input files");
        gbInputFiles.setLayout(new FormLayout());
        
        formData = new FormData();
        formData.top    = new FormAttachment(0,     MARGIN);
        formData.bottom = new FormAttachment(0, 	170);
        formData.left   = new FormAttachment(0,     MARGIN);
        formData.right  = new FormAttachment(100,  -MARGIN);        
        gbInputFiles.setLayoutData(formData);
        
        final Label laFCC_database = new Label(gbInputFiles, SWT.LEFT);
        laFCC_database.setText("FCC database filename (U.S. callsigns):");
        formData = new FormData();
        formData.top   = new FormAttachment(0, MARGIN);
        formData.left  = new FormAttachment(0, MARGIN);
        laFCC_database.setLayoutData(formData);
        
        txFCC_database = new Text(gbInputFiles, SWT.NONE);
        txFCC_database.setText("");
        formData = new FormData();
        formData.top   = new FormAttachment(laFCC_database, 5);
        formData.left  = new FormAttachment(0, MARGIN);
        formData.right = new FormAttachment(100, -(MARGIN + 25));
        txFCC_database.setLayoutData(formData);
        txFCC_database.addModifyListener(new ModifyListener() {			
			@Override public void modifyText(ModifyEvent arg0) { fccDbFilename = txFCC_database.getText(); }
		});
        
        final Label laCRTC_database = new Label(gbInputFiles, SWT.LEFT);
        laCRTC_database.setText("CRTC database filename (Canada callsigns):");
        formData = new FormData();
        formData.top   = new FormAttachment(laFCC_database, 40);
        formData.left  = new FormAttachment(0, MARGIN);
        laCRTC_database.setLayoutData(formData);
        
        txCRTC_database = new Text(gbInputFiles, SWT.NONE);
        txCRTC_database.setText("");
        formData = new FormData();
        formData.top   = new FormAttachment(laCRTC_database, 5);
        formData.left  = new FormAttachment(0, MARGIN);
        formData.right = new FormAttachment(100, -(MARGIN + 25));
        txCRTC_database.setLayoutData(formData);
        txCRTC_database.addModifyListener(new ModifyListener() {			
			@Override public void modifyText(ModifyEvent arg0) { crtcDbFilename = txCRTC_database.getText(); }
		});
        
        final Button bOpenFCC = new Button(gbInputFiles, SWT.PUSH);
        bOpenFCC.setText("...");
        formData = new FormData(20, 20);
        formData.top   = new FormAttachment(txFCC_database, -18);
        formData.left  = new FormAttachment(txFCC_database, 5);
        bOpenFCC.setLayoutData(formData);
        bOpenFCC.addSelectionListener(new SelectionListener() {
			@Override public void widgetSelected(SelectionEvent e) { onOpenDB(FileAction.OPEN_FCC_DB); }
			@Override public void widgetDefaultSelected(SelectionEvent e) { widgetSelected(e); }
		});
        
        final Button bOpenCRTC = new Button(gbInputFiles, SWT.PUSH);
        bOpenCRTC.setText("...");
        formData = new FormData(20, 20);
        formData.top   = new FormAttachment(txCRTC_database, -18);
        formData.left  = new FormAttachment(txCRTC_database, 5);
        bOpenCRTC.setLayoutData(formData);
        bOpenCRTC.addSelectionListener(new SelectionListener() {
			@Override public void widgetSelected(SelectionEvent e) { onOpenDB(FileAction.OPEN_CRTC_DB); }
			@Override public void widgetDefaultSelected(SelectionEvent e) { widgetSelected(e); }
		});
        
        final Group gbOutputFile = new Group(shell, SWT.SHADOW_IN);
        gbOutputFile.setText("Output file");
        gbOutputFile.setLayout(new FormLayout());
        
        formData = new FormData();
        formData.top    = new FormAttachment(gbInputFiles, MARGIN);
        formData.bottom = new FormAttachment(gbInputFiles, 265);
        formData.left   = new FormAttachment(0,     MARGIN);
        formData.right  = new FormAttachment(100,  -MARGIN);        
        gbOutputFile.setLayoutData(formData);
        
        final Label laOutputFile = new Label(gbOutputFile, SWT.LEFT);
        laOutputFile.setText("Output filename (Win-Test database file):");
        formData = new FormData();
        formData.top   = new FormAttachment(0, MARGIN);
        formData.left  = new FormAttachment(0, MARGIN);
        laOutputFile.setLayoutData(formData);
        
        txOutputFilename = new Text(gbOutputFile, SWT.NONE);
        txOutputFilename.setText("");
        formData = new FormData();
        formData.top   = new FormAttachment(laOutputFile, 5);
        formData.left  = new FormAttachment(0, MARGIN);
        formData.right = new FormAttachment(100, -(MARGIN + 25));
        txOutputFilename.setLayoutData(formData);
        txOutputFilename.addModifyListener(new ModifyListener() {			
			@Override public void modifyText(ModifyEvent arg0) { outputFilename = txOutputFilename.getText(); }
		});
        
        final Button bSaveOutput = new Button(gbOutputFile, SWT.PUSH);
        bSaveOutput.setText("...");
        formData = new FormData(20, 20);
        formData.top   = new FormAttachment(txOutputFilename, -18);
        formData.left  = new FormAttachment(txOutputFilename, 5);
        bSaveOutput.setLayoutData(formData);
        bSaveOutput.addSelectionListener(new SelectionListener() {
			@Override public void widgetSelected(SelectionEvent e) { onOpenDB(FileAction.SAVE_WT_DTB); }
			@Override public void widgetDefaultSelected(SelectionEvent e) { widgetSelected(e); }
		});
	}
	
	
	private void showUI() {
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		
		if (disposeDisplay) {
			display.dispose();
		}
	}
	
	
	public boolean hasData() {
		return 	StringUtils.isNotEmpty(fccDbFilename) && 
				StringUtils.isNotEmpty(crtcDbFilename) &&
				StringUtils.isNotEmpty(outputFilename);
	}
	
	
	private boolean canRun() {
		boolean b = hasData();
		if (!b) {
			statusLine.setText("Filename(s) missing.");
		}
		return b;
	}
	
	
	private void onRun() {
		if (!canRun()) {
			return;			
		}
		
		shell.dispose();
	}
	
	
	private void onOpenDB(FileAction fileAction) {
		FileSelector fileSelector = new FileSelector(fileAction);
		if (fileSelector.execute()) {
			switch (fileAction) {
			case OPEN_FCC_DB : 
				fccDbFilename  = fileSelector.getSelectedFileName();
				txFCC_database.setText(fccDbFilename);
				break;
			
			case OPEN_CRTC_DB: 
				crtcDbFilename = fileSelector.getSelectedFileName();
				txCRTC_database.setText(crtcDbFilename);
				break;
				
			case SAVE_WT_DTB:
				outputFilename = fileSelector.getSelectedFileName();
				txOutputFilename.setText(outputFilename);
				break;

			default:
				break;
			}
		}
	}
}
