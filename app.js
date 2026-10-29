/* =====================================================================
   SENTINEL — Interactive Agent Frontend (Spring Boot + OpenAI)
   ===================================================================== */

document.addEventListener('DOMContentLoaded', () => {

  // ---------- API Configuration & Key Management ----------
  const STORAGE_KEY = 'sentinel_openai_key';
  const STORAGE_MODEL = 'sentinel_openai_model';

  const getStoredApiKey = () => localStorage.getItem(STORAGE_KEY) || '';
  const setStoredApiKey = (key) => {
    if (key && key.trim()) {
      localStorage.setItem(STORAGE_KEY, key.trim());
    } else {
      localStorage.removeItem(STORAGE_KEY);
    }
  };

  const getStoredModel = () => localStorage.getItem(STORAGE_MODEL) || 'gpt-4o';
  const setStoredModel = (m) => localStorage.setItem(STORAGE_MODEL, m);

  const getApiHeaders = () => {
    const headers = { 'Accept': 'application/json' };
    const key = getStoredApiKey();
    if (key) {
      headers['X-OpenAI-Key'] = key;
    }
    return headers;
  };

  // ---------- Backend Agent Status Check ----------
  const backendDot = document.getElementById('backendDot');
  const backendText = document.getElementById('backendText');
  const modalStatusDot = document.getElementById('modalStatusDot');
  const modalStatusText = document.getElementById('modalStatusText');

  const updateBackendStatus = async () => {
    try {
      const res = await fetch('/api/agent/status', {
        headers: getApiHeaders()
      });
      if (!res.ok) throw new Error('Status endpoint unavailable');

      const data = await res.json();
      if (data.openAiConfigured) {
        if (backendDot) {
          backendDot.className = 'backend-dot backend-dot--openai';
        }
        if (backendText) {
          backendText.textContent = `Agent: OpenAI (${data.configuredModel || 'gpt-4o'})`;
        }
        if (modalStatusDot) {
          modalStatusDot.className = 'backend-dot backend-dot--openai';
        }
        if (modalStatusText) {
          modalStatusText.textContent = 'Active: OpenAI Agent ready for deep reasoning';
        }
      } else {
        if (backendDot) {
          backendDot.className = 'backend-dot backend-dot--local';
        }
        if (backendText) {
          backendText.textContent = 'Agent: Local Forensics';
        }
        if (modalStatusDot) {
          modalStatusDot.className = 'backend-dot backend-dot--local';
        }
        if (modalStatusText) {
          modalStatusText.textContent = 'Local Forensic Agent active (Enter key to enable OpenAI)';
        }
      }
    } catch (err) {
      console.warn('Backend status check failed:', err);
      if (backendDot) backendDot.className = 'backend-dot backend-dot--local';
      if (backendText) backendText.textContent = 'Agent: Local Mode';
      if (modalStatusDot) modalStatusDot.className = 'backend-dot backend-dot--local';
      if (modalStatusText) modalStatusText.textContent = 'Operating in offline local mode';
    }
  };

  updateBackendStatus();

  // ---------- OpenAI Settings Modal ----------
  const settingsModal = document.getElementById('settingsModal');
  const openSettingsBtn = document.getElementById('openSettingsBtn');
  const closeSettingsBtn = document.getElementById('closeSettingsBtn');
  const modalBackdrop = document.getElementById('modalBackdrop');
  const saveKeyBtn = document.getElementById('saveKeyBtn');
  const clearKeyBtn = document.getElementById('clearKeyBtn');
  const openaiKeyInput = document.getElementById('openaiKeyInput');
  const openaiModelSelect = document.getElementById('openaiModelSelect');

  const openModal = () => {
    if (openaiKeyInput) openaiKeyInput.value = getStoredApiKey();
    if (openaiModelSelect) openaiModelSelect.value = getStoredModel();
    if (settingsModal) {
      settingsModal.classList.add('active');
      settingsModal.setAttribute('aria-hidden', 'false');
    }
  };

  const closeModal = () => {
    if (settingsModal) {
      settingsModal.classList.remove('active');
      settingsModal.setAttribute('aria-hidden', 'true');
    }
  };

  if (openSettingsBtn) openSettingsBtn.addEventListener('click', openModal);
  if (closeSettingsBtn) closeSettingsBtn.addEventListener('click', closeModal);
  if (modalBackdrop) modalBackdrop.addEventListener('click', closeModal);

  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && settingsModal && settingsModal.classList.contains('active')) {
      closeModal();
    }
  });

  if (saveKeyBtn) {
    saveKeyBtn.addEventListener('click', async () => {
      const val = openaiKeyInput ? openaiKeyInput.value : '';
      const model = openaiModelSelect ? openaiModelSelect.value : 'gpt-4o';
      setStoredApiKey(val);
      setStoredModel(model);
      await updateBackendStatus();
      closeModal();
    });
  }

  if (clearKeyBtn) {
    clearKeyBtn.addEventListener('click', async () => {
      setStoredApiKey('');
      if (openaiKeyInput) openaiKeyInput.value = '';
      await updateBackendStatus();
      closeModal();
    });
  }

  // ---------- Scroll-based nav style ----------
  const nav = document.getElementById('nav');
  const handleNavScroll = () => {
    if (window.scrollY > 40) {
      nav.classList.add('nav--scrolled');
    } else {
      nav.classList.remove('nav--scrolled');
    }
  };
  window.addEventListener('scroll', handleNavScroll, { passive: true });

  // ---------- Intersection Observer for scroll reveal ----------
  const observerOptions = {
    threshold: 0.15,
    rootMargin: '0px 0px -60px 0px'
  };

  const revealObserver = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('visible');
      }
    });
  }, observerOptions);

  document.querySelectorAll('.pipeline__step').forEach((el, i) => {
    el.style.transitionDelay = `${i * 0.12}s`;
    revealObserver.observe(el);
  });

  document.querySelectorAll('.feature-card').forEach((el, i) => {
    el.classList.add('reveal');
    el.style.transitionDelay = `${i * 0.1}s`;
    revealObserver.observe(el);
  });

  document.querySelectorAll('.arch-block, .arch-tools').forEach(el => {
    el.classList.add('reveal');
    revealObserver.observe(el);
  });

  const distinction = document.querySelector('.distinction__inner');
  if (distinction) {
    distinction.classList.add('reveal');
    revealObserver.observe(distinction);
  }

  document.querySelectorAll('.section__header').forEach(header => {
    header.classList.add('reveal');
    revealObserver.observe(header);
  });

  // ---------- Demo Tabs (Preset vs Upload) ----------
  const tabPresetsBtn = document.getElementById('tabPresetsBtn');
  const tabUploadBtn = document.getElementById('tabUploadBtn');
  const demoGrid = document.getElementById('demo-grid');
  const uploadSection = document.getElementById('uploadSection');

  if (tabPresetsBtn && tabUploadBtn && demoGrid && uploadSection) {
    tabPresetsBtn.addEventListener('click', () => {
      tabPresetsBtn.classList.add('active');
      tabUploadBtn.classList.remove('active');
      demoGrid.style.display = 'grid';
      uploadSection.style.display = 'none';
    });

    tabUploadBtn.addEventListener('click', () => {
      tabUploadBtn.classList.add('active');
      tabPresetsBtn.classList.remove('active');
      demoGrid.style.display = 'none';
      uploadSection.style.display = 'block';
    });
  }

  // ---------- Live Preset Card Investigations ----------
  const demoCards = document.querySelectorAll('.demo-card');

  const executePresetInvestigation = async (card, sampleId) => {
    const analysisContainer = card.querySelector('.demo-card__analysis');
    if (!analysisContainer) return;

    try {
      const res = await fetch(`/api/investigate/preset/${sampleId}`, {
        method: 'POST',
        headers: getApiHeaders()
      });
      if (!res.ok) throw new Error('Investigation request failed');
      const report = await res.json();

      // Render updated live report data into card
      renderStepsIntoContainer(analysisContainer, report);
    } catch (err) {
      console.warn('Preset investigation request error:', err);
    }
  };

  const renderStepsIntoContainer = (container, report) => {
    if (!report || !report.steps) return;

    let html = '';
    report.steps.forEach((step, idx) => {
      if (idx === 4) return; // Step 5 is final verdict rendered below

      const iconClass = step.status === 'DONE' ? 'analysis-step__icon--done'
        : step.status === 'WARN' ? 'analysis-step__icon--warn'
        : 'analysis-step__icon--neutral';

      const textClass = step.status === 'DONE' && step.name === 'Signatures' ? 't-green'
        : step.status === 'WARN' ? 't-amber'
        : step.status === 'NEUTRAL' ? 't-neutral' : '';

      const checkSvg = step.status === 'DONE'
        ? '<svg viewBox="0 0 20 20" fill="currentColor" width="14" height="14"><path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"/></svg>'
        : step.status === 'WARN'
        ? '<svg viewBox="0 0 20 20" fill="currentColor" width="14" height="14"><path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd"/></svg>'
        : '<svg viewBox="0 0 20 20" fill="currentColor" width="14" height="14"><path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-8-3a1 1 0 00-.867.5 1 1 0 11-1.731-1A3 3 0 0113 8a3.001 3.001 0 01-2 2.83V11a1 1 0 11-2 0v-1a1 1 0 011-1 1 1 0 100-2zm0 8a1 1 0 100-2 1 1 0 000 2z" clip-rule="evenodd"/></svg>';

      html += `
        <div class="analysis-step" data-order="${step.stepNumber}">
          <div class="analysis-step__icon ${iconClass}">
            ${checkSvg}
          </div>
          <div class="analysis-step__text">
            <span class="analysis-step__label">${step.name}</span>
            <span class="analysis-step__detail ${textClass}">${step.summary}</span>
          </div>
        </div>
      `;
    });

    // Verdict Badge
    const verdict = report.verdict || 'UNKNOWN';
    const conf = report.confidenceScore || 50.0;
    const isVerified = verdict === 'VERIFIED';
    const isModified = verdict === 'MODIFIED';

    const resClass = isVerified ? 'analysis-result--verified' : isModified ? 'analysis-result--modified' : 'analysis-result--unknown';
    const barFillClass = isVerified ? 'confidence-bar__fill--green' : isModified ? 'confidence-bar__fill--amber' : 'confidence-bar__fill--neutral';

    html += `
      <div class="analysis-result ${resClass}">
        <div class="analysis-result__badge">
          ${verdict}
        </div>
        <div class="analysis-result__confidence">
          <span class="analysis-result__conf-label">Confidence</span>
          <div class="confidence-bar">
            <div class="confidence-bar__fill ${barFillClass}" style="--width: ${conf}%; width: ${conf}%;"></div>
          </div>
          <span class="analysis-result__conf-value">${conf.toFixed(1)}%</span>
        </div>
      </div>
      <div style="margin-top: 10px; font-size: 0.75rem; color: var(--text-dim); display: flex; justify-content: space-between;">
        <span>Agent: ${report.agentBackend === 'OPENAI' ? 'OpenAI (' + (report.modelUsed || 'gpt-4o') + ')' : 'Sentinel Forensics'}</span>
        <span>Latency: ${report.executionTimeMs || 12}ms</span>
      </div>
    `;

    container.innerHTML = html;
  };

  demoCards.forEach(card => {
    card.setAttribute('tabindex', '0');
    card.setAttribute('role', 'button');

    const handleCardClick = () => {
      const isActive = card.classList.contains('active');
      demoCards.forEach(c => c.classList.remove('active'));

      if (!isActive) {
        card.classList.add('active');
        const sampleId = card.getAttribute('data-status');
        if (sampleId) {
          executePresetInvestigation(card, sampleId);
        }

        setTimeout(() => {
          const rect = card.getBoundingClientRect();
          if (rect.bottom > window.innerHeight) {
            card.scrollIntoView({ behavior: 'smooth', block: 'center' });
          }
        }, 100);
      }
    };

    card.addEventListener('click', handleCardClick);
    card.addEventListener('keydown', (e) => {
      if (e.key === 'Enter' || e.key === ' ') {
        e.preventDefault();
        handleCardClick();
      }
    });
  });

  // ---------- Live Media Upload Dropzone ----------
  const dropzone = document.getElementById('dropzone');
  const mediaFileInput = document.getElementById('mediaFileInput');
  const uploadResultCard = document.getElementById('uploadResultCard');
  const uploadResultTitle = document.getElementById('uploadResultTitle');
  const uploadResultFile = document.getElementById('uploadResultFile');
  const uploadResultNarrative = document.getElementById('uploadResultNarrative');
  const uploadResultSteps = document.getElementById('uploadResultSteps');
  const uploadResultBadgeContainer = document.getElementById('uploadResultBadgeContainer');

  if (dropzone && mediaFileInput) {
    dropzone.addEventListener('click', () => mediaFileInput.click());

    dropzone.addEventListener('dragover', (e) => {
      e.preventDefault();
      dropzone.classList.add('dropzone--dragover');
    });

    dropzone.addEventListener('dragleave', () => {
      dropzone.classList.remove('dropzone--dragover');
    });

    dropzone.addEventListener('drop', (e) => {
      e.preventDefault();
      dropzone.classList.remove('dropzone--dragover');
      if (e.dataTransfer.files && e.dataTransfer.files[0]) {
        processUploadedFile(e.dataTransfer.files[0]);
      }
    });

    mediaFileInput.addEventListener('change', (e) => {
      if (e.target.files && e.target.files[0]) {
        processUploadedFile(e.target.files[0]);
      }
    });
  }

  const processUploadedFile = async (file) => {
    if (!uploadResultCard) return;

    uploadResultCard.style.display = 'block';
    if (uploadResultTitle) uploadResultTitle.textContent = 'Investigating Media...';
    if (uploadResultFile) uploadResultFile.textContent = `${file.name} (${(file.size / 1024).toFixed(1)} KB)`;
    if (uploadResultNarrative) uploadResultNarrative.textContent = 'Sentinel agent is extracting container bitstream, EXIF headers, and C2PA credentials...';
    if (uploadResultSteps) {
      uploadResultSteps.innerHTML = '<div style="color: var(--blue-400); font-family: var(--font-mono); font-size: 0.8125rem;">⟳ Executing Agent Pipeline...</div>';
    }

    const formData = new FormData();
    formData.append('file', file);

    try {
      const res = await fetch('/api/investigate/upload', {
        method: 'POST',
        headers: getApiHeaders(),
        body: formData
      });

      if (!res.ok) throw new Error(`HTTP error ${res.status}`);
      const report = await res.json();

      if (uploadResultTitle) uploadResultTitle.textContent = `Investigation Complete: ${report.verdict}`;
      if (uploadResultFile) {
        uploadResultFile.textContent = `${report.filename} · ${report.mediaType} · SHA-256: ${report.sha256 ? report.sha256.substring(0, 16) + '...' : 'N/A'}`;
      }
      if (uploadResultNarrative) {
        uploadResultNarrative.innerHTML = `<strong>Agent Findings:</strong> ${report.executiveSummary}<br/><br/>${report.narrative || ''}`;
      }
      if (uploadResultBadgeContainer) {
        uploadResultBadgeContainer.innerHTML = `
          <div class="upload-result__backend-tag">
            <svg viewBox="0 0 20 20" fill="currentColor" width="12" height="12"><path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z" clip-rule="evenodd"/></svg>
            <span>${report.agentBackend === 'OPENAI' ? 'OpenAI (' + (report.modelUsed || 'gpt-4o') + ')' : 'Local Forensic Engine'} · ${report.executionTimeMs}ms</span>
          </div>
        `;
      }

      renderStepsIntoContainer(uploadResultSteps, report);
      uploadResultCard.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    } catch (err) {
      console.error('File upload investigation error:', err);
      if (uploadResultNarrative) {
        uploadResultNarrative.innerHTML = `<span style="color: var(--rose-400);">Investigation failed: ${err.message}</span>`;
      }
    }
  };

  // ---------- Hero Terminal Live CLI Runner ----------
  const terminalBody = document.getElementById('terminal-body');
  const terminalTitle = document.getElementById('terminalTitle');
  const cliButtons = document.querySelectorAll('.quick-btn');

  const runTerminalInvestigation = async (presetId) => {
    if (!terminalBody) return;

    if (terminalTitle) {
      terminalTitle.textContent = `sentinel investigate --preset ${presetId}`;
    }

    terminalBody.innerHTML = `
      <div class="terminal__line terminal__line--prompt">$ sentinel investigate --preset ${presetId}</div>
      <div class="terminal__line terminal__line--info">⟳ Connecting to Sentinel Agent backend...</div>
    `;

    try {
      const res = await fetch(`/api/investigate/preset/${presetId}`, {
        method: 'POST',
        headers: getApiHeaders()
      });
      if (!res.ok) throw new Error('API request failed');
      const report = await res.json();

      const isVerified = report.verdict === 'VERIFIED';
      const isModified = report.verdict === 'MODIFIED';
      const verdictColorClass = isVerified ? 't-green' : isModified ? 't-amber' : 't-neutral';

      const s1 = report.steps[0] || {};
      const s2 = report.steps[1] || {};
      const s3 = report.steps[2] || {};
      const s4 = report.steps[3] || {};

      terminalBody.innerHTML = `
        <div class="terminal__line terminal__line--prompt">$ sentinel investigate --preset ${presetId}</div>
        <div class="terminal__line terminal__line--info anim-line" style="--delay: 0.2s">⟳ Inspecting media container & bitstream...</div>
        <div class="terminal__line anim-line" style="--delay: 0.5s">  <span class="t-dim">Format:</span> ${s1.summary || report.mediaType} <span class="t-dim">|</span> <span class="t-dim">SHA-256:</span> ${report.sha256 ? report.sha256.substring(0, 16) + '...' : 'b84a...'}</div>
        <div class="terminal__line terminal__line--info anim-line" style="--delay: 0.8s">⟳ Examining metadata & hardware EXIF...</div>
        <div class="terminal__line anim-line" style="--delay: 1.1s">  <span class="t-dim">Findings:</span> ${s2.summary || 'EXIF inspection complete'}</div>
        <div class="terminal__line terminal__line--success anim-line" style="--delay: 1.4s">✓ Verifying C2PA digital signatures...</div>
        <div class="terminal__line anim-line" style="--delay: 1.7s">  <span class="${verdictColorClass}">${s3.summary || 'C2PA check finished'}</span></div>
        <div class="terminal__line terminal__line--success anim-line" style="--delay: 2.0s">✓ Executing forensic scans (ELA + GAN detection)...</div>
        <div class="terminal__line anim-line" style="--delay: 2.3s">  <span class="t-dim">Forensics:</span> ${s4.summary || 'Forensic analysis clean'}</div>
        <div class="terminal__line terminal__line--result anim-line" style="--delay: 2.6s"></div>
        <div class="terminal__line terminal__line--result anim-line" style="--delay: 2.8s">  ╔══════════════════════════════════════════════════╗</div>
        <div class="terminal__line terminal__line--result anim-line" style="--delay: 3.0s">  ║  <span class="${verdictColorClass}">VERDICT: ${report.verdict}</span>                               ║</div>
        <div class="terminal__line terminal__line--result anim-line" style="--delay: 3.2s">  ║  Confidence: <span class="${verdictColorClass}">${report.confidenceScore ? report.confidenceScore.toFixed(1) + '%' : 'N/A'}</span> · Backend: <span class="t-accent">${report.agentBackend === 'OPENAI' ? 'OpenAI' : 'Local'}</span>    ║</div>
        <div class="terminal__line terminal__line--result anim-line" style="--delay: 3.4s">  ╚══════════════════════════════════════════════════╝</div>
        <div class="terminal__cursor anim-line" style="--delay: 3.8s"></div>
      `;
    } catch (err) {
      terminalBody.innerHTML += `<div class="terminal__line" style="color: var(--rose-400);">Error connecting to Sentinel backend: ${err.message}</div>`;
    }
  };

  cliButtons.forEach(btn => {
    btn.addEventListener('click', () => {
      cliButtons.forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      const preset = btn.getAttribute('data-cli');
      if (preset) {
        runTerminalInvestigation(preset);
      }
    });
  });

  // ---------- Smooth scroll for anchor links ----------
  document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', (e) => {
      const href = anchor.getAttribute('href');
      if (href === '#') return;

      e.preventDefault();
      const target = document.querySelector(href);
      if (target) {
        const offset = 80;
        const top = target.getBoundingClientRect().top + window.scrollY - offset;
        window.scrollTo({ top, behavior: 'smooth' });
      }
    });
  });

  // ---------- Auto-open first demo card when visible ----------
  const demoSection = document.getElementById('demo');
  let demoTriggered = false;

  const demoObserver = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting && !demoTriggered) {
        demoTriggered = true;
        setTimeout(() => {
          const verifiedCard = document.getElementById('demo-card-verified');
          if (verifiedCard && !document.querySelector('.demo-card.active')) {
            verifiedCard.click();
          }
        }, 500);
      }
    });
  }, { threshold: 0.3 });

  if (demoSection) {
    demoObserver.observe(demoSection);
  }

  // ---------- Parallax on hero gradient orbs ----------
  let ticking = false;
  window.addEventListener('mousemove', (e) => {
    if (ticking) return;
    ticking = true;

    requestAnimationFrame(() => {
      const x = (e.clientX / window.innerWidth - 0.5) * 2;
      const y = (e.clientY / window.innerHeight - 0.5) * 2;

      const orb1 = document.querySelector('.bg-gradient-orb--1');
      const orb2 = document.querySelector('.bg-gradient-orb--2');
      const orb3 = document.querySelector('.bg-gradient-orb--3');

      if (orb1) orb1.style.transform = `translate(${x * 20}px, ${y * 20}px)`;
      if (orb2) orb2.style.transform = `translate(${x * -15}px, ${y * 15}px)`;
      if (orb3) orb3.style.transform = `translate(${x * 10}px, ${y * -10}px)`;

      ticking = false;
    });
  });

  // ---------- Voiceover Audio Controls ----------
  const playVoiceoverBtn = document.getElementById('playVoiceoverBtn');
  const voiceoverPlayerBar = document.getElementById('voiceoverPlayerBar');
  const demoAudioPlayer = document.getElementById('demoAudioPlayer');
  const voiceoverToggleBtn = document.getElementById('voiceoverToggleBtn');
  const voiceoverIconPlay = document.getElementById('voiceoverIconPlay');
  const voiceoverIconPause = document.getElementById('voiceoverIconPause');
  const voiceoverTimeDisplay = document.getElementById('voiceoverTimeDisplay');
  const voiceoverCloseBtn = document.getElementById('voiceoverCloseBtn');

  if (playVoiceoverBtn && demoAudioPlayer && voiceoverPlayerBar) {
    const formatTime = (secs) => {
      const m = Math.floor(secs / 60);
      const s = Math.floor(secs % 60);
      return `${m}:${s < 10 ? '0' : ''}${s}`;
    };

    const togglePlayback = () => {
      if (demoAudioPlayer.paused) {
        demoAudioPlayer.play().catch(e => console.warn('Audio play failed:', e));
        voiceoverPlayerBar.style.display = 'flex';
        if (voiceoverIconPlay) voiceoverIconPlay.style.display = 'none';
        if (voiceoverIconPause) voiceoverIconPause.style.display = 'block';
      } else {
        demoAudioPlayer.pause();
        if (voiceoverIconPlay) voiceoverIconPlay.style.display = 'block';
        if (voiceoverIconPause) voiceoverIconPause.style.display = 'none';
      }
    };

    playVoiceoverBtn.addEventListener('click', togglePlayback);
    if (voiceoverToggleBtn) voiceoverToggleBtn.addEventListener('click', togglePlayback);

    demoAudioPlayer.addEventListener('timeupdate', () => {
      if (voiceoverTimeDisplay && demoAudioPlayer.duration) {
        voiceoverTimeDisplay.textContent = `${formatTime(demoAudioPlayer.currentTime)} / ${formatTime(demoAudioPlayer.duration)}`;
      }
    });

    demoAudioPlayer.addEventListener('ended', () => {
      if (voiceoverIconPlay) voiceoverIconPlay.style.display = 'block';
      if (voiceoverIconPause) voiceoverIconPause.style.display = 'none';
    });

    if (voiceoverCloseBtn) {
      voiceoverCloseBtn.addEventListener('click', () => {
        demoAudioPlayer.pause();
        demoAudioPlayer.currentTime = 0;
        voiceoverPlayerBar.style.display = 'none';
      });
    }
  }

});
